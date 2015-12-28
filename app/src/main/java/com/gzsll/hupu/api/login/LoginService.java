package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by sll on 2015/3/8.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/user/loginUsernameEmail")
    void login(@FieldMap Map<String, String> params, @Query("client") String client, Callback<LoginResult> callback);
}
