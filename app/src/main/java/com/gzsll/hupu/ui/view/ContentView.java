package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2015/3/7.
 */
public interface ContentView extends BaseView {

    void showLoading();

    void hideLoading();

    void renderContent(String url, List<String> urls);

    void renderShare(String share, String url);

    void isCollected(boolean isCollected);

    void onError(String error);

}
