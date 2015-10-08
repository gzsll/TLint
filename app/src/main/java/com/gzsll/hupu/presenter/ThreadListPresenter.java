package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.storage.bean.BaseResult;
import com.gzsll.hupu.storage.bean.GroupThread;
import com.gzsll.hupu.storage.bean.ThreadsResult;
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
    HuPuApi mHuPuApi;


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
        loadThreadList(groupId, "", type, list, true);
    }

    public void onRefresh() {
        view.onScrollToTop();
        loadThreadList(groupId, "", type, list, true);
    }


    public void onLoadMore() {
        loadThreadList(groupId, lastId, type, list, false);
    }


    private void loadThreadList(String groupId, String last, String type, List<String> list, final boolean clear) {
        mHuPuApi.getGroupThreadsList(groupId, last, type, list, new Callback<ThreadsResult>() {
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
        mHuPuApi.addGroupAttention(groupId, new Callback<BaseResult>() {
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
