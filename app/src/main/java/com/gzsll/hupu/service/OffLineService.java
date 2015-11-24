package com.gzsll.hupu.service;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.service.annotation.ActionMethod;
import com.gzsll.hupu.service.annotation.IntentAnnotationService;
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
import com.gzsll.hupu.support.storage.bean.ThreadHotReply;
import com.gzsll.hupu.support.storage.bean.ThreadInfo;
import com.gzsll.hupu.support.storage.bean.ThreadInfoResult;
import com.gzsll.hupu.support.storage.bean.ThreadReply;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.storage.bean.UserInfo;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.SecurityHelper;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;

/**
 * Created by gzsll on 2014/9/14 0014.
 */
public class OffLineService extends IntentAnnotationService {

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
    private long offlinePictureLength = 0;// 离线的图片总流量大小

    private int offlinePictureSize = 0;
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

    @Override
    public void onCreate() {
        super.onCreate();
        ((AppApplication) getApplication()).getObjectGraph().inject(this);
        logger.debug("服务启动");
    }

    @ActionMethod(START_DOWNLOAD)
    public void startOffline(Intent intent) {
        if (mCurrentStatus == INIT) {
            boards = (List<Board>) intent.getSerializableExtra("boards");
            prepareOffline();
        } else {
            //ignore
            logger.debug("服务已启动，忽略请求");
        }
    }

    private void prepareOffline() {
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
                int count = 100; //加载100篇帖子  TODO 加上设置
                ThreadsResult result = mThreadApi.getGroupThreadsList(String.valueOf(board.getBoardId()), "0", count, "", null);
                if (result.getStatus() == 200) {
                    List<GroupThread> threads = result.getData().getGroupThreads();
                    offlineThreadsCount += threads.size();
                    logger.debug("offlineThreadsCount:" + offlineThreadsCount);
                    mThreads.addAll(threads);
                    for (GroupThread thread : threads) {
                        saveThread(thread);

                        UserInfo userInfo = thread.getUserInfo();
                        if (userInfo != null) {
                            if (!isImageDownloaded(Uri.parse(userInfo.getIcon()))) {
                                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                                ImageRequest request = ImageRequest.fromUri(userInfo.getIcon());
                                imagePipeline.prefetchToDiskCache(request, this);
                                offlinePictureLength += request.getSourceFile().length();
                                offlinePictureCount++;
                            }

                        }
                    }

                    logger.debug(String.format("分组%s新增%d张待下载图片", board.getBoardName(), offlinePictureCount));
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
        List<DBGroupThread> threads = mGroupThreadDao.queryBuilder().where(DBGroupThreadDao.Properties.ServerId.eq(thread.getServerId())).list();
        DBGroupThread dbGroupThread = mDbConverterHelper.converGroupThread(thread);
        if (!threads.isEmpty()) {
            dbGroupThread.setId(threads.get(0).getId());
        }
        mGroupThreadDao.insertOrReplace(dbGroupThread);
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

        if (mThreads.isEmpty()) {
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
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ThreadInfoResult result = mThreadApi.getGroupThreadInfo(thread.getServerId(), 0, 1, true);
            if (result != null && result.getStatus() == 200) {
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

            return null;
        }
    }

    @Inject
    DBThreadInfoDao mThreadInfoDao;

    private void saveThreadInfo(ThreadInfo threadInfo) {
        List<DBThreadInfo> threadInfos = mThreadInfoDao.queryBuilder().where(DBThreadInfoDao.Properties.ServerId.eq(threadInfo.getServerId())).list();
        DBThreadInfo info = mDbConverterHelper.converThreadInfo(threadInfo);
        if (!threadInfos.isEmpty()) {
            info.setId(threadInfos.get(0).getId());
        }
        mThreadInfoDao.insertOrReplace(info);

    }

    @Inject
    DBThreadReplyItemDao mReplyDao;

    private void saveReplyItem(ThreadReplyItem replyItem, boolean isHot) {
        List<DBThreadReplyItem> replyItems = mReplyDao.queryBuilder().where(DBThreadReplyItemDao.Properties.ServerId.eq(replyItem.getId())).list();
        DBThreadReplyItem item = mDbConverterHelper.converThreadReplyItem(replyItem, isHot);
        if (!replyItems.isEmpty()) {
            item.setId(replyItems.get(0).getId());
        }
        mReplyDao.insertOrReplace(item);
    }
}
