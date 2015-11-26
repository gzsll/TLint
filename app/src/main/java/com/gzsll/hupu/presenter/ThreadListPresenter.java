package com.gzsll.hupu.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.DBGroupThread;
import com.gzsll.hupu.support.db.DBGroupThreadDao;
import com.gzsll.hupu.support.db.DBGroups;
import com.gzsll.hupu.support.db.DBGroupsDao;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.Info;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.view.ThreadListView;
import com.jockeyjs.util.BackgroundExecutor;
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


    private List<GroupThread> threads = new ArrayList<GroupThread>();


    private String groupId;
    private String lastId;
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

    public void onThreadReceive(String groupId, String type, List<String> list) {
        view.showLoading();
        this.type = type;
        this.groupId = groupId;
        this.list = list;
        loadThreadList("", true);
    }

    public void onRefresh() {
        view.onScrollToTop();
        loadThreadList("", true);
    }

    public void onReload() {
        loadThreadList(lastId, false);
    }


    public void onLoadMore() {
        loadThreadList(lastId, false);
    }


    private void loadThreadList(String last, boolean clear) {
        if (mNetWorkHelper.isWiFi(mContext)) {
            loadFromNet(last, clear);
        } else {
            loadFromDb(clear);
        }
    }

    private void loadFromNet(String last, final boolean clear) {
        mThreadApi.getGroupThreadsList(groupId, last, 20, type, list, new Callback<ThreadsResult>() {
            @Override
            public void success(ThreadsResult threadsResult, Response response) {
                if (clear) {
                    threads.clear();
                    view.onScrollToTop();
                }
                if (threadsResult.getStatus() == 200) {
                    if (threadsResult.getData().getInfo() != null) {
                        view.renderThreadInfo(threadsResult.getData().getInfo());
                    }
                    lastId = threadsResult.getData().getLastId();
                    addThreads(threadsResult.getData().getGroupThreads());
                    // threads.addAll(threadsResult.getData().getGroupThreads());
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
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (clear) {
                    threads.clear();
                }
                List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(groupId)).list();
                if (!groupsList.isEmpty()) {
                    renderInfo(mDbConverterHelper.convertDbGroupsToInfo(groupsList.get(0)));
                }
                List<DBGroupThread> threads = mThreadDao.queryBuilder().where(DBGroupThreadDao.Properties.GroupId.eq(groupId)).orderDesc(DBGroupThreadDao.Properties.CreateAtUnixTime).list();
                addThreads(mDbConverterHelper.covertDbGroupThreads(threads));
                renderThreads(threads.isEmpty());
            }
        });
    }


    private void renderInfo(final Info info) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.renderThreadInfo(info);
            }
        });

    }


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


    private void addThreads(List<GroupThread> threadList) {
        for (GroupThread thread : threadList) {
            boolean isContain = false;
            for (GroupThread thread1 : threads) {
                if (thread.getTid() == thread1.getTid()) {
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
        mThreadApi.addGroupAttention(groupId, new Callback<BaseResult>() {
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
