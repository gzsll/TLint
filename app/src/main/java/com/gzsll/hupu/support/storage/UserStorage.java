package com.gzsll.hupu.support.storage;


import com.gzsll.hupu.support.db.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sll on 2015/7/11.
 */
@Singleton
public class UserStorage {

    @Inject
    public UserStorage() {
    }


    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void logout() {
        user = null;
    }

    public boolean isLogin() {
        return user != null && user.getIsLogin();
    }

    public String getToken() {
        if (!isLogin()) {
            return null;
        }
        return user.getToken();
    }

    public String getUid() {
        if (!isLogin()) {
            return "";
        }
        return user.getUid();
    }


}
