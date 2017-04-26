package com.gzsll.hupu.ui.thread.recommend;

import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface RecommendThreadListContract {
    interface View extends BaseView {

        void showLoading();

        void showContent();

        void renderThreads(List<Thread> threads);

        void onError(String error);

        void onEmpty();

        void onLoadCompleted(boolean hasMore);

        void onRefreshCompleted();
    }

    interface Presenter extends BasePresenter<View> {
        void onThreadReceive();

        void onRefresh();

        void onReload();

        void onLoadMore();
    }
}
