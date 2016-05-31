package com.gzsll.hupu.ui.thread.special;

import android.support.annotation.NonNull;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.components.rx.RxThread;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity public class ThreadRecommendPresenter implements SpecialThreadListContract.Presenter {

  Logger logger = Logger.getLogger(ThreadRecommendPresenter.class.getSimpleName());
  private ForumApi mForumApi;
  private RxThread mRxThread;

  private PublishSubject<List<Thread>> mThreadSubject;
  private boolean isFirst = true;
  private List<Thread> threads = new ArrayList<>();

  private Subscription mSubscription;
  private SpecialThreadListContract.View mSpecialView;
  private String lastTid = "";
  private String lastTamp = "";
  private boolean hasNextPage = true;

  @Inject public ThreadRecommendPresenter(ForumApi mForumApi, RxThread mRxThread) {
    this.mForumApi = mForumApi;
    this.mRxThread = mRxThread;
    mThreadSubject = PublishSubject.create();
  }

  @Override public void onThreadReceive() {
    mSpecialView.hideLoading();
    mRxThread.getThreadListObservable(Constants.TYPE_RECOMMEND, mThreadSubject)
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            mSpecialView.showLoading();
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Thread>>() {
          @Override public void call(List<Thread> threads) {
            logger.debug("getThreadListObservable call:" + threads.size());
            ThreadRecommendPresenter.this.threads = threads;
            if (threads.isEmpty()) {
              if (!isFirst) {
                mSpecialView.onError("数据加载失败");
              }
              isFirst = false;
            } else {
              if (!threads.isEmpty()) {
                lastTid = threads.get(threads.size() - 1).getTid();
              }
              mSpecialView.renderThreads(threads);
              mSpecialView.onRefreshCompleted();
              mSpecialView.onLoadCompleted(hasNextPage);
            }
          }
        });
    loadRecommendList();
  }

  private void loadRecommendList() {
    mSubscription = mRxThread.getRecommendThreadList(lastTid, lastTamp, mThreadSubject)
        .subscribe(new Action1<ThreadListData>() {
          @Override public void call(ThreadListData threadListData) {
            if (threadListData != null && threadListData.result != null) {
              ThreadListResult data = threadListData.result;
              lastTamp = data.stamp;
              hasNextPage = data.nextPage;
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            if (threads.isEmpty()) {
              mSpecialView.onError("数据加载失败，请重试");
            } else {
              mSpecialView.onRefreshCompleted();
              mSpecialView.onLoadCompleted(hasNextPage);
              ToastUtils.showToast("数据加载失败，请重试");
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
      ToastUtils.showToast("没有更多了~");
      mSpecialView.onLoadCompleted(false);
      return;
    }
    loadRecommendList();
  }

  @Override public void attachView(@NonNull SpecialThreadListContract.View view) {
    mSpecialView = view;
  }

  @Override public void detachView() {
    if (mSubscription != null && !mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
    mSpecialView = null;
  }
}
