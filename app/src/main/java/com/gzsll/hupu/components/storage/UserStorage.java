package com.gzsll.hupu.components.storage;

import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.service.MessageService;
import com.gzsll.hupu.util.SettingPrefUtil;

/**
 * Created by sll on 2015/7/11.
 */

public class UserStorage {

    private Context mContext;

    public UserStorage(Context mContext) {
        this.mContext = mContext;
    }

    private String cookie;
    private String token;

    private User user;

    public User getUser() {
        return user;
    }

    public void login(User user) {
        this.user = user;
        SettingPrefUtil.setLoginUid(mContext, user.getUid());
        Intent intent = new Intent(mContext, MessageService.class);
        intent.setAction(MessageService.ACTION_GET);
        mContext.startService(intent);
    }

    public void logout() {
        if (user.getUid().equals(SettingPrefUtil.getLoginUid(mContext))) {
            SettingPrefUtil.setLoginUid(mContext, "");
        }
        user = null;
        cookie = "";
        token = "";
        removeCookie();
        Intent intent = new Intent(mContext, MessageService.class);
        intent.setAction(MessageService.ACTION_CLOSE);
        mContext.startService(intent);
    }

    private void removeCookie() {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    public boolean isLogin() {
        return user != null && SettingPrefUtil.getLoginUid(mContext).equals(user.getUid());
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
