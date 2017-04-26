package com.gzsll.hupu.ui.content;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/11.
 */
public interface ContentContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderContent(int page, int totalPage);

        void onUpdatePager(int page, int totalPage);

        void setCurrentItem(int index);

        void isCollected(boolean isCollected);

        void onError(String error);

        void onToggleFloatingMenu();

        void showLoginUi();

        void showReportUi();

        void showPostUi(String title);
    }

    interface Presenter extends BasePresenter<View> {

        void onThreadInfoReceive(String tid, String fid, String pid, int page);

        void onReload();

        void onRefresh();

        void onPageNext();

        void onPagePre();

        void onPageSelected(int page);

        void onCommendClick();

        void onShareClick();

        void onReportClick();

        void onCollectClick();

        void updatePage(int page);
    }
}
