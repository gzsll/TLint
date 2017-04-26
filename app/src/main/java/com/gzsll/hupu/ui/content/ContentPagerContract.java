package com.gzsll.hupu.ui.content;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/25.
 */
public interface ContentPagerContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError();

        void sendMessageToJS(String handlerName, Object object);

        void loadUrl(String url);

        void showReplyUi(String fid, String tid, String pid, String title);

        void showReportUi(String tid, String pid);

        void showBrowserUi(String url);

        void showContentUi(String tid, String pid, int page);

        void showThreadListUi(String fid);

        void showUserProfileUi(String uid);

        void showLoginUi();

        void onClose();
    }

    interface Presenter extends BasePresenter<View> {

        void onThreadInfoReceive(String tid, String fid, String pid, int page);

        void onReply(int area, int index);

        void onReport(int area, int index);

        void addLight(int area, int index);

        void addRuLight(int area, int index);

        void handlerUrl(String url);

        void onReload();

        ContentPagerPresenter.HupuBridge getJavaScriptInterface();
    }
}
