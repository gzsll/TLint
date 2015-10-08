package com.gzsll.hupu.view;

import com.gzsll.hupu.storage.bean.UserInfo;

/**
 * Created by sll on 2015/9/14.
 */
public interface UserProfileView extends BaseView {

    void renderUserInfo(UserInfo userInfo);
}
