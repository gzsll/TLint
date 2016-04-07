package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/9.
 */
public interface ThreadListView extends BaseView {

    void showLoading();

    void hideLoading();

    void renderThreadInfo(Forum forum);

    void renderThreads(List<Thread> threads);

    void onLoadCompleted(boolean hasMore);

    void onRefreshCompleted();

    void attendStatus(boolean isAttention);

    void onError(String error);

    void onEmpty();

    void onScrollToTop();

    void onFloatingVisibility(int visibility);


}
