package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.ThreadCollectView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
public class ThreadCollectPresenter extends Presenter<ThreadCollectView> {


    @Inject
    GameApi mGameApi;
    @Inject
    ToastHelper mToastHelper;

    @Singleton
    @Inject
    public ThreadCollectPresenter() {
    }


    private Subscription mSubscription;
    private List<Thread> threads = new ArrayList<>();
    private int page = 1;

    public void onCollectThreadsReceive() {
        view.showLoading();
        loadCollectList(page);
    }

    private void loadCollectList(final int page) {
        this.page = page;
        mSubscription = mGameApi.getCollectList(page).map(new Func1<ThreadListResult, List<Thread>>() {
            @Override
            public List<Thread> call(ThreadListResult result) {
                if (page == 1) {
                    threads.clear();
                }
                if (result != null && result.result != null) {
                    ThreadListData data = result.result;
                    return addThreads(data.data);
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> threads) {
                if (threads != null) {
                    if (threads.isEmpty()) {
                        view.onEmpty();
                    } else {
                        view.onRefreshing(false);
                        view.hideLoading();
                        view.renderThreads(threads);
                    }
                } else {
                    loadThreadError();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadThreadError();
            }
        });
    }

    private void loadThreadError() {
        if (threads.isEmpty()) {
            view.onError("数据加载失败");
        } else {
            view.hideLoading();
            view.onRefreshing(false);
            mToastHelper.showToast("数据加载失败");
        }
    }

    private List<Thread> addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            if (!contains(thread)) {
                threads.add(thread);
            }
        }
        return threads;
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
        page = 1;
        loadCollectList(page);
    }


    public void onReload() {
        view.showLoading();
        loadCollectList(page);
    }

    public void onLoadMore() {
        loadCollectList(++page);
    }


    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        page = 1;
        threads.clear();
    }
}
