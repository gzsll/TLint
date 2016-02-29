package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by sll on 2015/3/8.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("user/loginUsernameEmail")
    Observable<LoginResult> login(@FieldMap Map<String, String> params, @Query("client") String client);
}
