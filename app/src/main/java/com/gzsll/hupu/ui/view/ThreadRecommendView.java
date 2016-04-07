package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/9.
 */
public interface ThreadRecommendView extends BaseView {


    void showLoading();

    void hideLoading();


    void renderThreads(List<Thread> threads);

    void onLoadCompleted(boolean hasMore);

    void onRefreshCompleted();


    void onError(String error);

    void onEmpty();


}
