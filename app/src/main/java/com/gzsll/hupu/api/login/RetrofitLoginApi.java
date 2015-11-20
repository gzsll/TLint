package com.gzsll.hupu.api.login;

import com.gzsll.hupu.support.storage.bean.LoginResult;
import com.squareup.okhttp.OkHttpClient;

import org.apache.log4j.Logger;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by sll on 2015/3/8.
 */
public class RetrofitLoginApi implements LoginAPi {
    Logger logger = Logger.getLogger("RetrofitLoginApi");
    private LoginService loginService;

    public RetrofitLoginApi(OkHttpClient okHttpClient) {
        RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(okHttpClient))
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        loginService = restAdapter.create(LoginService.class);
    }


    @Override
    public void login(String userName, String passWord, final LoginCallBack callBack) {
        loginService.login(userName, passWord, new Callback<LoginResult>() {
            @Override
            public void success(LoginResult result, Response response) {
                callBack.onFinish(result);
            }

            @Override
            public void failure(RetrofitError error) {
                callBack.onError(error.getMessage());
            }
        });
    }
}
