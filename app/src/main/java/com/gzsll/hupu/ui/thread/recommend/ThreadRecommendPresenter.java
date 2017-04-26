package com.gzsll.hupu.ui.thread.recommend;

import android.support.annotation.NonNull;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.data.ThreadRepository;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class ThreadRecommendPresenter implements RecommendThreadListContract.Presenter {


    private ThreadRepository mThreadRepository;

    private PublishSubject<List<Thread>> mThreadSubject;
    private boolean isFirst = true;
    private List<Thread> threads = new ArrayList<>();

    private Subscription mSubscription;
    private RecommendThreadListContract.View mRecommendView;
    private String lastTid = "";
    private String lastTamp = "";
    private boolean hasNextPage = true;

    @Inject
    public ThreadRecommendPresenter(ThreadRepository mThreadRepository) {
        this.mThreadRepository = mThreadRepository;
        mThreadSubject = PublishSubject.create();
    }

    @Override
    public void onThreadReceive() {
        mRecommendView.showLoading();
        mThreadRepository.getThreadListObservable(Constants.TYPE_RECOMMEND, mThreadSubject)
                .subscribe(new Action1<List<Thread>>() {
                    @Override
                    public void call(List<Thread> threads) {
                        ThreadRecommendPresenter.this.threads = threads;
                        if (threads.isEmpty()) {
                            if (!isFirst) {
                                mRecommendView.onError("数据加载失败");
                            }
                            isFirst = false;
                        } else {
                            mRecommendView.showContent();
                            if (!threads.isEmpty()) {
                                lastTid = threads.get(threads.size() - 1).getTid();
                            }
                            mRecommendView.renderThreads(threads);

                        }
                    }
                });
        loadRecommendList();
    }

    private void loadRecommendList() {
        mSubscription = mThreadRepository.getRecommendThreadList(lastTid, lastTamp, mThreadSubject)
                .subscribe(new Action1<ThreadListData>() {
                    @Override
                    public void call(ThreadListData threadListData) {
                        if (threadListData != null && threadListData.result != null) {
                            ThreadListResult data = threadListData.result;
                            lastTamp = data.stamp;
                            hasNextPage = data.nextPage;
                        }
                        mRecommendView.onRefreshCompleted();
                        mRecommendView.onLoadCompleted(hasNextPage);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (threads.isEmpty()) {
                            mRecommendView.onError("数据加载失败，请重试");
                        } else {
                            mRecommendView.onRefreshCompleted();
                            mRecommendView.onLoadCompleted(hasNextPage);
                            ToastUtil.showToast("数据加载失败，请重试");
                        }
                    }
                });
    }

    public void onRefresh() {
        lastTamp = "";
        lastTid = "";
        loadRecommendList();
    }

    public void onReload() {
        onThreadReceive();
    }

    public void onLoadMore() {
        if (!hasNextPage) {
            ToastUtil.showToast("没有更多了~");
            mRecommendView.onLoadCompleted(false);
            return;
        }
        loadRecommendList();
    }

    @Override
    public void attachView(@NonNull RecommendThreadListContract.View view) {
        mRecommendView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mRecommendView = null;
    }
}
