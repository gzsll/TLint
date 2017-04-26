package com.gzsll.hupu.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.Logger;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.ThreadLightReplyData;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadReplyData;
import com.gzsll.hupu.bean.ThreadReplyQuote;
import com.gzsll.hupu.bean.ThreadReplyResult;
import com.gzsll.hupu.components.notifier.OfflineNotifier;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.db.ThreadDao;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadInfoDao;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.db.ThreadReplyDao;
import com.gzsll.hupu.injector.component.DaggerServiceComponent;
import com.gzsll.hupu.injector.module.ServiceModule;
import com.gzsll.hupu.util.ConfigUtil;
import com.gzsll.hupu.util.FileUtil;
import com.gzsll.hupu.util.NetWorkUtil;
import com.gzsll.hupu.util.SettingPrefUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sll on 2016/5/30.
 */
public class OffLineService extends Service {
    public static final String START_DOWNLOAD = BuildConfig.APPLICATION_ID + ".action.START_DOWNLOAD";
    public static final String EXTRA_FORUMS = "forums";

    @Inject
    ForumApi mForumApi;
    @Inject
    ForumDao mForumDao;
    @Inject
    ThreadDao mThreadDao;
    @Inject
    OfflineNotifier mOfflineNotifier;
    @Inject
    ThreadReplyDao mReplyDao;
    @Inject
    ThreadInfoDao mThreadInfoDao;
    @Inject
    OkHttpHelper mOkHttpHelper;

    public static final int INIT = 0;
    public static final int PREPARE = 1;
    public static final int LOAD_THREAD = 2;
    public static final int LOAD_REPLY = 3;
    public static final int LOAD_PICTURE = 4;
    public static final int CANCEL = 5;
    public static final int FINISHED = 6;

    private int mCurrentStatus = INIT;

    private List<Forum> forums;
    private List<Forum> unOfflineForums;
    private int offlineThreadsCount = 0;// 离线的帖子数量
    private int offlineRepliesCount = 0; //离线的回复数量

    private long offlineThreadsLength = 0;// 离线的帖子总流量大小
    private long offlineRepliesLength = 0;
    private long offlinePictureLength = 0;// 离线的图片总流量大小

    private int offlinePictureCount = 0;// 离线的图片数量

    private LinkedBlockingQueue<Thread> threadList = new LinkedBlockingQueue<>();// 线程安全队列

    private Map<String, String> lastTidMap = new HashMap<>();
    private Map<String, String> lastStmMap = new HashMap<>();
    private Map<String, Integer> threadCountMap = new HashMap<>();
    private static final int TOTAL_COUNT = 100;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("服务初始化");
        DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return super.onStartCommand(intent, flags, startId);
        }
        String action = intent.getAction();
        if (action.equals(START_DOWNLOAD)) {
            if (mCurrentStatus == INIT) {
                forums = (List<Forum>) intent.getSerializableExtra(EXTRA_FORUMS);
                prepareOffline();
            } else {
                //ignore
                Logger.d("服务已启动，忽略请求");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void prepareOffline() {
        if (forums == null || forums.isEmpty()) {
            return;
        }
        mCurrentStatus = PREPARE;
        unOfflineForums = new ArrayList<>();
        unOfflineForums.addAll(forums);
        for (final Forum forum : unOfflineForums) {
            if (isCanceled()) {
                return;
            }
            getThreadList(forum, "", "");
        }
    }

    private void getThreadList(final Forum forum, final String lastTid, final String lastStm) {
        final String fid = forum.getFid();
        if (threadCountMap.get(fid) != null && threadCountMap.get(fid) >= TOTAL_COUNT) {
            mOfflineNotifier.notifyThreads(forum, offlineThreadsLength);
            unOfflineForums.remove(forum);
            prepareReplies();
            return;
        }
        Subscription mSubscription = mForumApi.getThreadsList(fid, lastTid, lastStm,
                SettingPrefUtil.getThreadSort(OffLineService.this))
                .doOnNext(new Action1<ThreadListData>() {
                    @Override
                    public void call(ThreadListData threadListData) {
                        if (threadListData != null && threadListData.result != null) {
                            List<Thread> threads = threadListData.result.data;
                            offlineThreadsCount += threads.size();
                            offlineThreadsLength += JSON.toJSONString(threadListData).length();
                            lastStmMap.put(fid, threadListData.result.stamp);
                            threadList.addAll(threads);
                            lastTidMap.put(fid, threads.get(threads.size() - 1).getTid());
                            Integer count = threadCountMap.get(fid);
                            if (count == null) {
                                count = 0;
                            }
                            threadCountMap.put(fid, count + threads.size());
                            if (!threads.isEmpty()) {
                                int type = Integer.valueOf(threads.get(0).getFid());
                                if (TextUtils.isEmpty(lastTid) && TextUtils.isEmpty(lastStm)) {
                                    mThreadDao.queryBuilder()
                                            .where(ThreadDao.Properties.Type.eq(type))
                                            .buildDelete()
                                            .executeDeleteWithoutDetachingEntities();
                                }
                                for (Thread thread : threads) {
                                    if (mThreadDao.queryBuilder()
                                            .where(ThreadDao.Properties.Tid.eq(thread.getTid()),
                                                    ThreadDao.Properties.Type.eq(type))
                                            .count() == 0) {
                                        thread.setType(type);
                                        mThreadDao.insert(thread);
                                    }
                                }
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThreadListData>() {
                    @Override
                    public void call(ThreadListData threadListData) {
                        getThreadList(forum, lastTidMap.get(fid), lastStmMap.get(fid));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getThreadList(forum, lastTidMap.get(fid), lastStmMap.get(fid));
                    }
                });
        mCompositeSubscription.add(mSubscription);
    }

    private boolean isCanceled() {
        return mCurrentStatus == CANCEL || mCurrentStatus == FINISHED;
    }

    private void prepareReplies() {
        if (!unOfflineForums.isEmpty()) {
            return;
        }
        mOfflineNotifier.notifyThreadsSuccess(forums.size(), offlineThreadsCount, offlineThreadsLength);
        if (threadList.isEmpty()) {
            stopSelf();
        } else {
            for (final Thread thread : threadList) {
                Subscription mSubscription =
                        Observable.zip(mForumApi.getThreadInfo(thread.getTid(), thread.getFid(), 1, ""),
                                mForumApi.getThreadReplyList(thread.getTid(), thread.getFid(), 1),
                                mForumApi.getThreadLightReplyList(thread.getTid(), thread.getFid()),
                                new Func3<ThreadInfo, ThreadReplyData, ThreadLightReplyData, Boolean>() {
                                    @Override
                                    public Boolean call(ThreadInfo threadInfo, ThreadReplyData threadReplyData,
                                                        ThreadLightReplyData threadLightReplyData) {
                                        if (threadInfo != null) {
                                            offlineRepliesLength += JSON.toJSONString(threadInfo).length();
                                            threadInfo.setForumName(threadInfo.getForum().getName());
                                            mThreadInfoDao.queryBuilder()
                                                    .where(ThreadInfoDao.Properties.Tid.eq(thread.getTid()))
                                                    .buildDelete()
                                                    .executeDeleteWithoutDetachingEntities();
                                            mThreadInfoDao.insert(threadInfo);
                                            transImgToLocal(threadInfo.getContent());
                                        }

                                        if (threadReplyData != null && threadReplyData.status == 200) {
                                            offlineRepliesLength += JSON.toJSONString(threadReplyData).length();
                                            ThreadReplyResult result = threadReplyData.result;
                                            if (result != null && !result.list.isEmpty()) {
                                                for (ThreadReply reply : result.list) {
                                                    reply.setTid(thread.getTid());
                                                    reply.setPageIndex(1);
                                                    saveReply(reply, false);
                                                }
                                            }
                                        }

                                        if (threadLightReplyData != null && threadLightReplyData.status == 200) {
                                            offlineRepliesLength += JSON.toJSONString(threadLightReplyData).length();
                                            if (!threadLightReplyData.list.isEmpty()) {
                                                for (ThreadReply reply : threadLightReplyData.list) {
                                                    saveReply(reply, true);
                                                }
                                            }
                                        }
                                        return true;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        offlineReplyComplete(thread);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        throwable.printStackTrace();
                                        offlineReplyComplete(thread);
                                    }
                                });
                mCompositeSubscription.add(mSubscription);
            }
        }
    }

    private void offlineReplyComplete(Thread thread) {
        mOfflineNotifier.notifyReplies(thread, offlineRepliesLength);
        threadList.remove(thread);
        if (threadList.isEmpty()) {
            Logger.d("mThreads.isEmpty():" + offlineRepliesCount);
            mOfflineNotifier.notifyRepliesSuccess(offlineThreadsCount, offlineRepliesCount,
                    offlineRepliesLength);
            stopSelf();
        }
        stopSelfIfCan();
    }

    private boolean stopSelfIfCan() {
        if (isCanceled()) {
            Logger.d("isCanceled");
            stopSelf();
            return true;
        }

        if (!NetWorkUtil.isWifiConnected(this)) {
            Logger.d("isWiFi");
            mCurrentStatus = CANCEL;
            stopSelf();
            return true;
        }
        return false;
    }

    private void saveReply(ThreadReply reply, boolean isLight) {
        offlineRepliesCount++;
        if (!reply.getQuote().isEmpty()) {
            ThreadReplyQuote quote = reply.getQuote().get(0);
            reply.setQuoteHeader(quote.header.get(0));
            if (!TextUtils.isEmpty(quote.togglecontent)) {
                reply.setQuoteToggle(quote.togglecontent);
            }
            reply.setQuoteContent(quote.content);
        }
        reply.setIsLight(isLight);
        mReplyDao.queryBuilder()
                .where(ThreadReplyDao.Properties.Pid.eq(reply.getPid()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        mReplyDao.insert(reply);

        transImgToLocal(reply.getContent());
        transImgToLocal(reply.getQuoteContent());
        transGifToLocal(reply.getContent());
        transGifToLocal(reply.getQuoteContent());
    }

    private void transImgToLocal(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Pattern pattern = Pattern.compile("<img(.+?)data_url=\"(.+?)\"(.+?)src=\"(.+?)\"(.+?)>");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String thumb = matcher.group(4);
            String original = matcher.group(2);
            cacheImage(thumb, original);
        }
        matcher.reset();
    }

    private void transGifToLocal(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Pattern pattern = Pattern.compile("<img(.+?)data-gif=\"(.+?)\"(.+?)src=\"(.+?)\"(.+?)>");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String thumb = matcher.group(4);
            String original = matcher.group(2);
            cacheImage(thumb, original);
        }
        matcher.reset();
    }

    private void cacheImage(String thumb, String original) {
        cacheImage(original);
        String localUrl = thumb.substring(thumb.lastIndexOf("/") + 1);
        String localPath = ConfigUtil.getCachePath() + File.separator + localUrl;
        if (!FileUtil.exist(localPath)) {
            try {
                mOkHttpHelper.httpDownload(thumb, new File(localPath));
            } catch (Exception e) {
                Log.d("HtmlUtils", "图片下载失败:" + thumb);
            }
            offlinePictureCount++;
            offlinePictureLength += new File(localPath).length();
        }
    }

    private void cacheImage(String url) {
        if (!isImageDownloaded(Uri.parse(url))) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            ImageRequest request = ImageRequest.fromUri(url);
            imagePipeline.prefetchToDiskCache(request, this);
            offlinePictureLength += request.getSourceFile().length();
            offlinePictureCount++;
        }
    }

    private boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)
                || ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        mOfflineNotifier.notifyPictureSuccess(offlinePictureCount, offlinePictureLength);
        mCurrentStatus = FINISHED;
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
