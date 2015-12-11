package com.gzsll.hupu.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.DBGroupThreadDao;
import com.gzsll.hupu.support.db.DBGroupsDao;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.view.ThreadListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/3/4.
 */
public class ThreadListPresenter extends Presenter<ThreadListView> {

    private Logger logger = Logger.getLogger(ThreadListPresenter.class.getSimpleName());
    @Inject
    Bus bus;
    @Inject
    ThreadApi mThreadApi;
    @Inject
    NetWorkHelper mNetWorkHelper;
    @Inject
    Context mContext;
    @Inject
    DBGroupThreadDao mThreadDao;
    @Inject
    DBGroupsDao mGroupsDao;
    @Inject
    DbConverterHelper mDbConverterHelper;

    private Handler handler = new Handler(Looper.getMainLooper());


    private List<Thread> threads = new ArrayList<Thread>();


    private String fid;
    private String lastTid;
    private String type;
    private List<String> list;


    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        //for onPause()
    }

    @Override
    public void destroy() {

    }

    public void onThreadReceive(String fid, String type, List<String> list) {
        view.showLoading();
        this.type = type;
        this.fid = fid;
        this.list = list;
        loadThreadList("", true);
        getAttendStatus();
    }


    private void getAttendStatus() {
        mThreadApi.getGroupAttentionStatus(fid, new Callback<AttendStatusResult>() {
            @Override
            public void success(AttendStatusResult baseResult, Response response) {
                if (baseResult.status == 200) {
                    view.renderThreadInfo(baseResult.forumInfo);
                    view.attendStatus(baseResult.status);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void onRefresh() {
        view.onScrollToTop();
        loadThreadList("", true);
    }

    public void onReload() {
        loadThreadList(lastTid, false);
    }


    public void onLoadMore() {
        loadThreadList(lastTid, false);
    }


    private void loadThreadList(String last, boolean clear) {
        if (mNetWorkHelper.isFast()) {
            loadFromNet(last, clear);
        } else {
            loadFromDb(clear);
        }
    }

    private void loadFromNet(String last, final boolean clear) {
        mThreadApi.getGroupThreadsList(fid, last, 20, type, list, new Callback<ThreadListResult>() {
            @Override
            public void success(ThreadListResult result, Response response) {
                if (clear) {
                    threads.clear();
                    view.onScrollToTop();
                }

                if (result.result != null && !result.result.data.isEmpty()) {
                    addThreads(result.result.data);
                    view.renderThreads(threads);
                    view.hideLoading();
                } else {
                    if (threads.isEmpty()) {
                        view.onError("数据加载失败");
                    } else {
                        view.showToast("数据加载失败");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logger.debug(error.getMessage());
                view.onError(error.getMessage());
            }
        });
    }

    private void loadFromDb(final boolean clear) {
//        BackgroundExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                if (clear) {
//                    threads.clear();
//                }
//                List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(groupId)).list();
//                if (!groupsList.isEmpty()) {
//                    renderInfo(mDbConverterHelper.convertDbGroupsToInfo(groupsList.get(0)));
//                }
//                List<DBGroupThread> threads = mThreadDao.queryBuilder().where(DBGroupThreadDao.Properties.GroupId.eq(groupId)).orderDesc(DBGroupThreadDao.Properties.CreateAtUnixTime).list();
//                addThreads(mDbConverterHelper.covertDbGroupThreads(threads));
//                renderThreads(threads.isEmpty());
//            }
//        });
    }


//    private void renderInfo(final Info info) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                view.renderThreadInfo(info);
//            }
//        });
//
//    }


    private void renderThreads(final boolean empty) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!empty) {
                    view.renderThreads(threads);
                    view.hideLoading();
                } else {
                    view.onEmpty();
                }
            }
        });
    }


    private void addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            boolean isContain = false;
            for (Thread thread1 : threads) {
                if (thread.tid.equals(thread1.tid)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                threads.add(thread);
            }
        }
    }


    public void addAttention() {
        mThreadApi.addGroupAttention(fid, new Callback<BaseResult>() {
            @Override
            public void success(BaseResult baseResult, Response response) {
                if (baseResult != null) {
                    view.showToast(baseResult.getMsg());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


}
