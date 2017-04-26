package com.gzsll.hupu.api.login;

import com.gzsll.hupu.bean.CookieData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sll on 2016/3/8.
 */
public interface CookieService {

    @FormUrlEncoded
    @POST("member.action")
    Observable<CookieData> login(
            @Field("username") String username, @Field("password") String password);
}
