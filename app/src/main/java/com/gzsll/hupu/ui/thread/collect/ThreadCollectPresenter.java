package com.gzsll.hupu.ui.thread.collect;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
@PerActivity
public class ThreadCollectPresenter implements CollectThreadListContract.Presenter {

    private GameApi mGameApi;

    private CollectThreadListContract.View mSpecialView;
    private Subscription mSubscription;
    private List<Thread> threads = new ArrayList<>();
    private int page = 1;
    private boolean hasNextPage = true;

    @Inject
    public ThreadCollectPresenter(GameApi gameApi) {
        mGameApi = gameApi;
    }

    @Override
    public void onThreadReceive() {
        mSpecialView.showLoading();
        loadCollectList(page);
    }

    private void loadCollectList(final int page) {
        this.page = page;
        mSubscription = mGameApi.getCollectList(page).map(new Func1<ThreadListData, List<Thread>>() {
            @Override
            public List<Thread> call(ThreadListData result) {
                if (page == 1) {
                    threads.clear();
                }
                if (result != null && result.result != null) {
                    ThreadListResult data = result.result;
                    hasNextPage = data.nextDataExists == 1;
                    threads.addAll(data.data);
                    return threads;
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> threads) {
                if (threads != null) {
                    if (threads.isEmpty()) {
                        mSpecialView.onEmpty();
                    } else {
                        mSpecialView.onLoadCompleted(hasNextPage);
                        mSpecialView.onRefreshCompleted();
                        mSpecialView.hideLoading();
                        mSpecialView.renderThreads(threads);
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
            mSpecialView.onError("数据加载失败");
        } else {
            mSpecialView.hideLoading();
            mSpecialView.onLoadCompleted(true);
            mSpecialView.onRefreshCompleted();
            ToastUtil.showToast("数据加载失败");
        }
    }

    public void onRefresh() {
        page = 1;
        loadCollectList(page);
    }

    public void onReload() {
        mSpecialView.showLoading();
        loadCollectList(page);
    }

    public void onLoadMore() {
        if (!hasNextPage) {
            ToastUtil.showToast("没有更多了~");
            mSpecialView.onLoadCompleted(false);
            return;
        }
        loadCollectList(++page);
    }

    @Override
    public void attachView(@NonNull CollectThreadListContract.View view) {
        mSpecialView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSpecialView = null;
    }
}
