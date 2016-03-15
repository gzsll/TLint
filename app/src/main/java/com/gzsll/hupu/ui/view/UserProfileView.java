package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.UserResult;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/3/11.
 */
public interface UserProfileView extends BaseView {

    void renderUserData(UserResult userResult);

    void showLoading();

    void hideLoading();

    void showError();


}
