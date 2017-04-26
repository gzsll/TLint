package com.gzsll.hupu.ui.thread.list;

import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface ThreadListContract {
    interface View extends BaseView {
        void showLoading();

        void showProgress();

        void showContent();

        void renderThreadInfo(Forum forum);

        void renderThreads(List<Thread> threads);

        void onLoadCompleted(boolean hasMore);

        void onRefreshCompleted();

        void attendStatus(boolean isAttention);

        void onError(String error);

        void onEmpty();

        void onScrollToTop();

        void onFloatingVisibility(int visibility);

        void showPostThreadUi(String fid);

        void showLoginUi();

        void showToast(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        void onThreadReceive(String type);

        void onStartSearch(String key, int page);

        void onAttentionClick();

        void onPostClick();

        void onRefresh();

        void onReload();

        void onLoadMore();
    }
}
