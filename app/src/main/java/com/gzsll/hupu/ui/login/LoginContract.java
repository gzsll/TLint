package com.gzsll.hupu.ui.login;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/11.
 */
public interface LoginContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void showUserNameError(String error);

        void showPassWordError(String error);

        void loginSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void login(String userName, String passWord);
    }
}
