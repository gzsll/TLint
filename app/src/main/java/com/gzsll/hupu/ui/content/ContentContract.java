package com.gzsll.hupu.ui.content;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface ContentContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderContent(String url, List<String> urls);

        void renderShare(String share, String url);

        void isCollected(boolean isCollected);

        void onError(String error);
    }

    interface Presenter extends BasePresenter<View> {
        void onThreadInfoReceive(String tid, String fid, String pid, int page);

        void onReload();

        void onRefresh();

        void onPageNext();

        void onPagePre();

        void onPageSelected(int page);

        void updatePage(int page);

        void addCollect();

        void delCollect();

        void copy(String stripped);

        void onShare(String text);


    }
}
