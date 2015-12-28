package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.squareup.okhttp.OkHttpClient;

import org.apache.log4j.Logger;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by sll on 2015/3/8.
 */
public class LoginApi {
    Logger logger = Logger.getLogger("RetrofitLoginApi");
    private LoginService loginService;

    private RequestHelper requestHelper;

    private static final String BASE_URL = "http://games.mobileapi.hupu.com/1/7.0.5";

    public LoginApi(OkHttpClient okHttpClient, RequestHelper requestHelper) {
        this.requestHelper = requestHelper;
        RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(okHttpClient))
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        loginService = restAdapter.create(LoginService.class);
    }


    public void login(String userName, String passWord, Callback<LoginResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("username", userName);
        params.put("password", passWord);
        String sign = requestHelper.getRequestSignV2(params);
        params.put("sign", sign);
        loginService.login(params, "123333", callback);

    }
}
