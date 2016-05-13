package com.gzsll.hupu.ui.thread.special;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class ThreadRecommendPresenter implements SpecialThreadListContract.Presenter {


    private ForumApi mForumApi;


    private List<Thread> threads = new ArrayList<>();


    private SpecialThreadListContract.View mSpecialView;
    private String lastTid = "";
    private String lastTamp = "";
    private boolean hasNextPage = true;

    private Subscription mSubscription;

    @Inject
    public ThreadRecommendPresenter(ForumApi forumApi) {
        mForumApi = forumApi;
    }

    @Override
    public void onThreadReceive() {
        mSpecialView.showLoading();
        loadRecommendList(false);
    }


    private void loadRecommendList(final boolean clear) {
        mSubscription = mForumApi.getRecommendThreadList(lastTid, lastTamp).map(new Func1<ThreadListData, List<Thread>>() {
            @Override
            public List<Thread> call(ThreadListData result) {
                if (clear) {
                    threads.clear();
                }
                if (result != null && result.result != null) {
                    ThreadListResult data = result.result;
                    lastTamp = data.stamp;
                    hasNextPage = data.nextPage;
                    return addThreads(data.data);
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> threads) {
                if (threads != null) {
                    mSpecialView.hideLoading();
                    mSpecialView.renderThreads(threads);
                    mSpecialView.onRefreshCompleted();
                    mSpecialView.onLoadCompleted(hasNextPage);
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
            mSpecialView.onRefreshCompleted();
            mSpecialView.onLoadCompleted(true);
            ToastUtils.showToast("数据加载失败");
        }
    }

    private List<Thread> addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            if (!contains(thread)) {
                threads.add(thread);
            }
        }
        if (!threads.isEmpty()) {
            lastTid = threads.get(threads.size() - 1).tid;
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
        lastTamp = "";
        lastTid = "";
        loadRecommendList(true);
    }


    public void onReload() {
        onThreadReceive();
    }

    public void onLoadMore() {
        if (!hasNextPage) {
            ToastUtils.showToast("没有更多了~");
            mSpecialView.onLoadCompleted(false);
            return;
        }
        loadRecommendList(false);
    }


    @Override
    public void attachView(@NonNull SpecialThreadListContract.View view) {
        mSpecialView = view;
    }

    @Override
    public void detachView() {
        lastTamp = "";
        lastTid = "";
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        threads.clear();
    }

}
