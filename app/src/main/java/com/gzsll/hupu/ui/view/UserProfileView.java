package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.UserData;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/3/11.
 */
public interface UserProfileView extends BaseView {

    void renderUserData(UserData userData);

    void showLoading();

    void hideLoading();

    void showError();


}
