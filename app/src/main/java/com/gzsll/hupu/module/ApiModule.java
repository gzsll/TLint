package com.gzsll.hupu.module;

import com.gzsll.hupu.api.cookie.CookieApi;
import com.gzsll.hupu.api.login.LoginApi;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

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
    public LoginApi providesLoginApi(OkHttpClient okHttpClient, RequestHelper requestHelper) {
        return new LoginApi(okHttpClient, requestHelper);
    }


    @Provides
    @Singleton
    public ThreadApi provideHuPuApi(UserStorage userStorage, OkHttpClient okHttpClient, RequestHelper requestHelper, SettingPrefHelper settingPrefHelper) {
        return new ThreadApi(requestHelper,settingPrefHelper,userStorage,okHttpClient);
    }


    @Provides
    @Singleton
    public CookieApi providesCookieApi(OkHttpClient okHttpClient){
        return new CookieApi(okHttpClient);
    }


}
