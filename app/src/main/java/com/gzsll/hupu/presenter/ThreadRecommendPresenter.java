package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.view.ThreadRecommendView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2015/12/12.
 */
public class ThreadRecommendPresenter extends Presenter<ThreadRecommendView> {


    @Inject
    ThreadApi mThreadApi;

    private String lastTid = "";
    private String lastTamp = "";
    private boolean clear;

    private List<Thread> threads = new ArrayList<Thread>();

    public void onRecommendListReceive() {
        view.showLoading();
        clear = true;
        loadRecommendList();
    }

    private void loadRecommendList() {
        mThreadApi.getRecommendThreadList(lastTid,lastTamp).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ThreadListResult>() {
            @Override
            public void call(ThreadListResult result) {
                if (clear) {
                    threads.clear();
                    view.onScrollToTop();
                }
                if (result.result != null) {
                    lastTamp = result.result.stamp;
                    addThreads(result.result.data);
                    view.renderList(threads);
                    view.hideLoading();
                } else {
                    if (threads.isEmpty()) {
                        view.onError("数据加载失败");
                    } else {
                        view.onRefreshing(false);
                        view.showToast("数据加载失败");
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (threads.isEmpty()) {
                    view.onError("数据加载失败");
                } else {
                    view.onRefreshing(false);
                    view.showToast("数据加载失败,请检查网络后重试");
                }
            }
        });
    }

    private void addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            if (!contains(thread)) {
                threads.add(thread);
            }
        }
        lastTid = threads.get(threads.size() - 1).tid;
    }


    private boolean contains(Thread thread) {
        boolean isContain = false;
        for (Thread thread1 : threads) {
            if (thread.tid.equals(thread1.tid)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }


    public void onRefresh() {
        lastTamp = "";
        lastTid = "";
        clear = true;
        loadRecommendList();
    }

    public void onLoadMore() {
        clear = false;
        loadRecommendList();
    }


    public void onReload() {
        clear = false;
        loadRecommendList();
    }


    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
