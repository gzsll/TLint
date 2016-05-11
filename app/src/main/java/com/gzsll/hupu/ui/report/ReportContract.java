package com.gzsll.hupu.ui.report;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/11.
 */
public interface ReportContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void reportSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void submitReports(String tid, String pid, String type, String content);
    }
}
