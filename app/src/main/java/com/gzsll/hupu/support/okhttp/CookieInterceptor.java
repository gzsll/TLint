package com.gzsll.hupu.support.okhttp;

import android.text.TextUtils;

import com.gzsll.hupu.support.storage.UserStorage;

import org.apache.log4j.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sll on 2016/2/23.
 */
public class CookieInterceptor implements Interceptor {

    Logger logger = Logger.getLogger(CookieInterceptor.class.getSimpleName());

    private UserStorage mUserStorage;

    public CookieInterceptor(UserStorage mUserStorage) {
        this.mUserStorage = mUserStorage;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (!TextUtils.isEmpty(mUserStorage.getCookie())) {
            Request request = original.newBuilder().addHeader("Cookie", "u=" + mUserStorage.getCookie() + ";").build();
            return chain.proceed(request);
        } else {
            for (String header : chain.proceed(original).headers("Set-Cookie")) {
                if (header.startsWith("u=")) {
                    String token = header.split(";")[0].substring(2);
                    logger.debug("token:" + token);
                    if (!TextUtils.isEmpty(token)) {
                        mUserStorage.setCookie(token);
                    }
                }
            }
        }
        return chain.proceed(original);
    }
}
