package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by sll on 2015/3/8.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/login")
    void login(@Field("username") String userName, @Field("password") String PassWord, Callback<LoginResult> callback);
}
