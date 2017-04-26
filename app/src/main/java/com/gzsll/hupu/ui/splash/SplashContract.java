package com.gzsll.hupu.ui.splash;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/31.
 */
public interface SplashContract {
    interface View extends BaseView {
        void showMainUi();
    }

    interface Presenter extends BasePresenter<View> {
        void initUmeng();

        void initHuPuSign();
    }
}
