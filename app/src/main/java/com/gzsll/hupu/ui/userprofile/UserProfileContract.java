package com.gzsll.hupu.ui.userprofile;

import com.gzsll.hupu.bean.UserResult;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/11.
 */
public class UserProfileContract {

    interface View extends BaseView {
        void renderUserData(UserResult userResult);

        void showLoading();

        void hideLoading();

        void showError();
    }

    interface Presenter extends BasePresenter<View> {
        void receiveUserInfo();
    }
}
