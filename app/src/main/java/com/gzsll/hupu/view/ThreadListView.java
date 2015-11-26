package com.gzsll.hupu.view;

import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.Info;

import java.util.List;

/**
 * Created by sll on 2015/3/4.
 */
public interface ThreadListView extends BaseView {

    void renderThreadInfo(Info info);

    void renderThreads(final List<GroupThread> threads);

    void onError(String error);

    void onEmpty();

    void onScrollToTop();

    void showLoginView();
}
