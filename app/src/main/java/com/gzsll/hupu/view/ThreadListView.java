package com.gzsll.hupu.view;

import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.storage.bean.Thread;

import java.util.List;

/**
 * Created by sll on 2015/3/4.
 */
public interface ThreadListView extends BaseView {

    void renderThreadInfo(Board info);

    void renderThreads(List<Thread> threads);

    void attendStatus(int status);

    void onError(String error);

    void onEmpty();

    void onScrollToTop();

    void onFloatingVisibility(int visibility);

    void onRefreshing(boolean refresh);


}
