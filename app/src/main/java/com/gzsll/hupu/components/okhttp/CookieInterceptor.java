package com.gzsll.hupu.components.okhttp;

import android.text.TextUtils;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.components.storage.UserStorage;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sll on 2016/2/23.
 */
public class CookieInterceptor implements Interceptor {


    private UserStorage mUserStorage;

    public CookieInterceptor(UserStorage mUserStorage) {
        this.mUserStorage = mUserStorage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (!TextUtils.isEmpty(mUserStorage.getCookie()) && !original.url()
                .toString()
                .contains("loginUsernameEmail")) {
            Request request = original.newBuilder()
                    .addHeader("Cookie", "u=" + URLEncoder.encode(mUserStorage.getCookie()) + ";")
                    .build();
            return chain.proceed(request);
        } else {
            for (String header : chain.proceed(original).headers("Set-Cookie")) {
                if (header.startsWith("u=")) {
                    String cookie = header.split(";")[0].substring(2);
                    if (!TextUtils.isEmpty(cookie)) {
                        Constants.Cookie = cookie;
                    }
                }
            }
        }
        return chain.proceed(original);
    }
}
