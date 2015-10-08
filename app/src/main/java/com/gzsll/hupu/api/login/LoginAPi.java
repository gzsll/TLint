package com.gzsll.hupu.api.login;

import com.gzsll.hupu.storage.bean.LoginResult;

/**
 * Created by sll on 2015/3/8.
 */
public interface LoginAPi {
    static String BASE_URL = "http://mobileapi.hupu.com/1/1.1.1/passport";

    void login(String userName, String passWord, LoginCallBack callBack);

    public interface LoginCallBack {
        void onFinish(LoginResult result);

        void onError(String errorMessage);
    }

}
