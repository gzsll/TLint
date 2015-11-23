package com.gzsll.hupu.service;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.service.annotation.ActionMethod;
import com.gzsll.hupu.service.annotation.IntentAnnotationService;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.notifier.OfflineNotifier;
import com.gzsll.hupu.support.storage.bean.Badge;
import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.OfflinePicture;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.storage.bean.UserInfo;
import com.gzsll.hupu.support.utils.SecurityHelper;

import org.apache.log4j.Logger;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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

    private long offlineThreadsLength = 0;// 离线的帖子总流量大小
    private long offlinePictureLength = 0;// 离线的图片总流量大小
    private Map<String, String> mPictureMap = new HashMap<>();// 用来去重
    private LinkedBlockingQueue<OfflinePicture> mPictures = new LinkedBlockingQueue<>();// 线程安全队列

    @Inject
    ThreadApi mThreadApi;
    @Inject
    OfflineNotifier mOfflineNotifier;
    @Inject
    SecurityHelper mSecurityHelper;

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
            // 发生异常了重复加载几次
            int repeat = 3;
            while (--repeat >= 0) {
                if (isCanceled()) {
                    break;
                }
                int count = 100; //加载100篇帖子  TODO 加上设置
                ThreadsResult result = mThreadApi.getGroupThreadsList(String.valueOf(board.getGroupId()), "0", count, "", null);
                if (result.getStatus() == 200) {
                    offlineThreadsLength += result.getData().getGroupThreads().size();
                    logger.debug("offlineThreadsLength:" + offlineThreadsLength);

                    final List<OfflinePicture> pictureList = new ArrayList<>();
                    for (GroupThread thread : result.getData().getGroupThreads()) {
                        saveThread(thread);

                        final UserInfo userInfo = thread.getUserInfo();
                        if (userInfo != null) {
                            if (!mPictureMap.containsKey(mSecurityHelper.getMD5(userInfo.getIcon()))) {
                                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                                DataSource<Boolean> dataSource = imagePipeline.isInDiskCache(Uri.parse(userInfo.getIcon()));
                                DataSubscriber<Boolean> subscriber = new BaseDataSubscriber<Boolean>() {
                                    @Override
                                    protected void onNewResultImpl(DataSource<Boolean> dataSource) {
                                        if (!dataSource.isFinished()) {
                                            return;
                                        }
                                        Boolean isInCache = dataSource.getResult();
                                        if (isInCache != null && isInCache) {
                                            OfflinePicture picture = new OfflinePicture();
                                            picture.setUrl(userInfo.getIcon());
                                            pictureList.add(picture);
                                            mPictureMap.put(mSecurityHelper.getMD5(userInfo.getIcon()), userInfo.getIcon());
                                        }

                                    }

                                    @Override
                                    protected void onFailureImpl(DataSource<Boolean> dataSource) {

                                    }
                                };
                                dataSource.subscribe(subscriber, new ScheduledThreadPoolExecutor(1));
                            }
                        }


                    }
                    return true;
                } else {
                    return false;
                }


            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            unOfflineBoards.remove(board);

        }

        private void saveThread(GroupThread thread) {
            UserInfo userInfo = thread.getUserInfo();
            userInfo.save();
            Badge badge = userInfo.getBadge();
            if (badge != null) {
                badge.save();
            }
            DataSupport.saveAll(thread.getCover());

            thread.save();

        }
    }
}
