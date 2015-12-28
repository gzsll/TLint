package com.gzsll.hupu.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;

import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.db.DBGroupThread;
import com.gzsll.hupu.support.db.DBGroupThreadDao;
import com.gzsll.hupu.support.db.DBThreadInfo;
import com.gzsll.hupu.support.db.DBThreadInfoDao;
import com.gzsll.hupu.support.db.DBThreadReplyItem;
import com.gzsll.hupu.support.db.DBThreadReplyItemDao;
import com.gzsll.hupu.support.db.DBUserInfoDao;
import com.gzsll.hupu.support.notifier.OfflineNotifier;
import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.OfflinePictureInfo;
import com.gzsll.hupu.support.storage.bean.ThreadHotReply;
import com.gzsll.hupu.support.storage.bean.ThreadInfo;
import com.gzsll.hupu.support.storage.bean.ThreadInfoResult;
import com.gzsll.hupu.support.storage.bean.ThreadReply;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.storage.bean.ThreadSpanned;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.storage.bean.UserInfo;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.HtmlHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.support.utils.ReplyViewHelper;
import com.gzsll.hupu.support.utils.SecurityHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;

/**
 * Created by gzsll on 2014/9/14 0014.
 */
public class OffLineService extends Service {

    Logger logger = Logger.getLogger("OffLineService");


    public static final String START_DOWNLOAD = BuildConfig.APPLICATION_ID + ".action.START_DOWNLOAD";


    public static final int INIT = 0;
    public static final int PREPARE = 1;
    public static final int LOAD_THREAD = 2;
    public static final int LOAD_REPLY = 3;
    public static final int LOAD_PICTURE = 4;
    public static final int CANCEL = 5;
    public static final int FINISHED = 6;

    private int mCurrentStatus = INIT;

    private List<Board> boards;
    private List<Board> unOfflineBoards;
    private int offlineThreadsCount;// 离线的帖子数量
    private int offlineRepliesCount; //离线的回复数量

    private long offlineThreadsLength = 0;// 离线的帖子总流量大小
    private long offlineRepliesLength = 0;
    private long offlinePictureLength = 0;// 离线的图片总流量大小

    private int offlinePictureCount = 0;// 离线的图片数量

    private LinkedBlockingQueue<GroupThread> mThreads = new LinkedBlockingQueue<>();// 线程安全队列


    @Inject
    ThreadApi mThreadApi;
    @Inject
    OfflineNotifier mOfflineNotifier;
    @Inject
    SecurityHelper mSecurityHelper;
    @Inject
    DBGroupThreadDao mGroupThreadDao;
    @Inject
    DBUserInfoDao mUserInfoDao;
    @Inject
    DbConverterHelper mDbConverterHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        ((AppApplication) getApplication()).getObjectGraph().inject(this);
        logger.debug("服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || TextUtils.isEmpty(intent.getAction()))
            return super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        if (action.equals(START_DOWNLOAD)) {
            if (mCurrentStatus == INIT) {
                boards = (List<Board>) intent.getSerializableExtra("boards");
                prepareOffline();
            } else {
                //ignore
                logger.debug("服务已启动，忽略请求");
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }


    private void prepareOffline() {
        if (boards == null || boards.isEmpty()) {
            return;
        }
        mCurrentStatus = PREPARE;
        unOfflineBoards = new ArrayList<>();
        unOfflineBoards.addAll(boards);
        for (Board board : unOfflineBoards) {
            new LoadThreadTask(board).execute();
        }

    }

    private boolean isCanceled() {
        return mCurrentStatus == CANCEL || mCurrentStatus == FINISHED;
    }

    @Inject
    Gson mGson;

    class LoadThreadTask extends AsyncTask<Void, Void, Boolean> {

        private Board board;

        LoadThreadTask(Board board) {
            this.board = board;
            mOfflineNotifier.notifyThreads(board, offlineThreadsLength);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (isCanceled()) {
                    return false;
                }
                int count = mSettingPrefHelper.getOfflineCount(); //加载帖子
                ThreadsResult result = mThreadApi.getGroupThreadsList(String.valueOf(board.getFid()), "0", count, mSettingPrefHelper.getThreadSort(), null);
                if (result.getStatus() == 200) {
                    List<GroupThread> threads = result.getData().getGroupThreads();
                    offlineThreadsCount += threads.size();
                    offlineThreadsLength += mGson.toJson(result).length();

                    logger.debug("offlineThreadsCount:" + offlineThreadsCount);
                    mThreads.addAll(threads);
                    for (GroupThread thread : threads) {
                        saveThread(thread);
                        UserInfo userInfo = thread.getUserInfo();
                        if (userInfo != null) {
                            cacheImage(userInfo.getIcon());
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // 更新广播
            if (result) {
                mOfflineNotifier.notifyThreads(board, offlineThreadsLength);
            }

            unOfflineBoards.remove(board);
            prepareReplies();
        }

    }

    private void saveThread(GroupThread thread) {
        List<DBGroupThread> threads = mGroupThreadDao.queryBuilder().where(DBGroupThreadDao.Properties.ServerId.eq(thread.getId())).list();
        DBGroupThread dbGroupThread = mDbConverterHelper.convertGroupThread(thread);
        if (!threads.isEmpty()) {
            dbGroupThread.setId(threads.get(0).getId());
        }
        mGroupThreadDao.insertOrReplace(dbGroupThread);
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
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        return ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey);
    }

    private synchronized void prepareReplies() {
        if (!unOfflineBoards.isEmpty()) {
            return;
        }

        mOfflineNotifier.notifyThreadsSuccess(boards.size(), offlineThreadsCount, offlineThreadsLength);

        if (mThreads.isEmpty()) {
            logger.debug("mThreads.isEmpty()");
            stopSelf();
        } else {
            for (GroupThread thread : mThreads) {
                new LoadRepliesTask(thread).execute();
            }
        }


    }


    class LoadRepliesTask extends AsyncTask<Void, Void, Boolean> {

        private GroupThread thread;

        LoadRepliesTask(GroupThread thread) {
            this.thread = thread;
            mOfflineNotifier.notifyReplies(thread, offlineRepliesLength);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ThreadInfoResult result = mThreadApi.getGroupThreadInfo(thread.getId(), 0, 1, true);
                if (result != null && result.getStatus() == 200) {
                    offlineRepliesLength += mGson.toJson(result).length();

                    saveThreadInfo(result.getData().getThreadInfo());

                    ThreadHotReply hotReply = result.getData().getThreadHotReply();
                    if (hotReply != null && !hotReply.getList().isEmpty()) {
                        for (ThreadReplyItem replyItem : hotReply.getList()) {
                            saveReplyItem(replyItem, true);

                        }
                    }
                    ThreadReply reply = result.getData().getThreadReply();
                    if (reply != null && !reply.getList().isEmpty()) {
                        for (ThreadReplyItem replyItem : reply.getList()) {
                            saveReplyItem(replyItem, false);
                        }
                    }

                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mOfflineNotifier.notifyReplies(thread, offlineRepliesLength);
            mThreads.remove(thread);
            if (mThreads.isEmpty()) {
                mOfflineNotifier.notifyRepliesSuccess(offlineThreadsCount, offlineRepliesCount, offlineRepliesLength);
                logger.debug("mThreads.isEmpty()");
                stopSelf();
            }
            stopSelfIfCan();
        }
    }

    @Inject
    NetWorkHelper mNetWorkHelper;

    private boolean stopSelfIfCan() {
        if (isCanceled()) {
            logger.debug("isCanceled");
            stopSelf();
            return true;
        }

        if (!mNetWorkHelper.isWiFi()) {
            logger.debug("isWiFi");
            stopSelf();
            mCurrentStatus = CANCEL;
            return true;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy");
        mOfflineNotifier.notifyPictureSuccess(offlinePictureCount, offlinePictureLength);
        mCurrentStatus = FINISHED;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Inject
    DBThreadInfoDao mThreadInfoDao;
    @Inject
    HtmlHelper mHtmlHelper;

    private void saveThreadInfo(ThreadInfo threadInfo) {
        List<DBThreadInfo> threadInfos = mThreadInfoDao.queryBuilder().where(DBThreadInfoDao.Properties.ServerId.eq(threadInfo.getId())).list();
        DBThreadInfo info = mDbConverterHelper.convertThreadInfo(threadInfo);
        if (!threadInfos.isEmpty()) {
            info.setId(threadInfos.get(0).getId());
        }
        mThreadInfoDao.insertOrReplace(info);
        OfflinePictureInfo pictureInfo = mHtmlHelper.downloadImgToLocal(threadInfo.getContent());
        offlinePictureCount += pictureInfo.getOfflineCount();
        offlinePictureLength += pictureInfo.getOfflineLength();
    }

    @Inject
    DBThreadReplyItemDao mReplyDao;
    @Inject
    ReplyViewHelper mReplyViewHelper;

    private void saveReplyItem(ThreadReplyItem replyItem, boolean isHot) {
        List<DBThreadReplyItem> replyItems = mReplyDao.queryBuilder().where(DBThreadReplyItemDao.Properties.ServerId.eq(replyItem.getId())).list();
        DBThreadReplyItem item = mDbConverterHelper.convertThreadReplyItem(replyItem, isHot);
        if (!replyItems.isEmpty()) {
            item.setId(replyItems.get(0).getId());
        }
        mReplyDao.insertOrReplace(item);
        offlineRepliesCount++;

        List<ThreadSpanned> spanneds = mReplyViewHelper.compileContent(replyItem.getContent());
        for (ThreadSpanned spanned : spanneds) {
            if (spanned.type == 2) {
                cacheImage(spanned.realContent);
            }
        }
    }
}
