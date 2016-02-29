package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;
import com.gzsll.hupu.support.utils.RequestHelper;

import org.apache.log4j.Logger;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Created by sll on 2015/3/8.
 */
public class LoginApi {
    Logger logger = Logger.getLogger("RetrofitLoginApi");
    private LoginService mLoginService;

    private RequestHelper requestHelper;

    private static final String BASE_URL = "http://games.mobileapi.hupu.com/1/7.0.5/";

    public LoginApi(OkHttpClient mOkHttpClient, RequestHelper requestHelper) {
        this.requestHelper = requestHelper;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mLoginService = retrofit.create(LoginService.class);
    }


    public Observable<LoginResult> login(String userName, String passWord) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("username", userName);
        params.put("password", passWord);
        String sign = requestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mLoginService.login(params, "123333").subscribeOn(Schedulers.io());

    }
}
