package com.gzsll.hupu.components.storage;


import com.gzsll.hupu.db.User;
import com.gzsll.hupu.helper.SettingPrefHelper;

/**
 * Created by sll on 2015/7/11.
 */

public class UserStorage {

    private SettingPrefHelper mSettingPrefHelper;


    public UserStorage(SettingPrefHelper mSettingPrefHelper) {
        this.mSettingPrefHelper = mSettingPrefHelper;
    }


    private String cookie;
    private String token;

    private User user;

    public User getUser() {
        return user;
    }


    public void login(User user) {
        this.user = user;
        mSettingPrefHelper.setLoginUid(user.getUid());
    }


    public void logout() {
        if (user.getUid().equals(mSettingPrefHelper.getLoginUid())) {
            mSettingPrefHelper.setLoginUid("");
        }
        user = null;
    }

    public boolean isLogin() {
        return user != null && mSettingPrefHelper.getLoginUid().equals(user.getUid());
    }

    public String getToken() {
        if (!isLogin()) {
            return token;
        }
        return user.getToken();
    }

    public String getUid() {
        if (!isLogin()) {
            return "";
        }
        return user.getUid();
    }


    public String getCookie() {
        if (isLogin()) {
            return user.getCookie();
        }
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
