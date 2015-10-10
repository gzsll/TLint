package com.gzsll.hupu.module;

import com.google.gson.Gson;
import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.api.login.LoginAPi;
import com.gzsll.hupu.api.login.RetrofitLoginApi;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2015/3/7.
 */
@Module(
        complete = false,
        library = true
)
public class ApiModule {


    @Provides
    @Singleton
    public LoginAPi providesLoginApi(OkHttpClient okHttpClient) {
        return new RetrofitLoginApi(okHttpClient);
    }


    @Provides
    @Singleton
    public HuPuApi provideHuPuApi(UserStorage userStorage, OkHttpClient okHttpClient, RequestHelper requestHelper, Gson gson) {
        return new HuPuApi(userStorage, okHttpClient, requestHelper, gson);
    }


}
