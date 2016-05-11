package com.gzsll.hupu.injector.module;


import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.api.login.CookieApi;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by sll on 2015/3/7.
 */
@Module
public class ApiModule {


    @Provides
    @Singleton
    public ForumApi provideHuPuApi(UserStorage userStorage, OkHttpClient okHttpClient, RequestHelper requestHelper, SettingPrefHelper settingPrefHelper) {
        return new ForumApi(requestHelper, settingPrefHelper, userStorage, okHttpClient);
    }


    @Provides
    @Singleton
    public GameApi provideGameApi(RequestHelper requestHelper, OkHttpClient okHttpClient) {
        return new GameApi(requestHelper, okHttpClient);
    }


    @Provides
    @Singleton
    public CookieApi providesCookieApi(OkHttpClient okHttpClient) {
        return new CookieApi(okHttpClient);
    }


}
