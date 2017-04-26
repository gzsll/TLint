package com.gzsll.hupu.injector.module;

import android.content.Context;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.api.login.CookieApi;
import com.gzsll.hupu.components.retrofit.RequestHelper;
import com.gzsll.hupu.components.storage.UserStorage;

import javax.inject.Named;
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
    public ForumApi provideHuPuApi(UserStorage userStorage, @Named("api") OkHttpClient okHttpClient,
                                   RequestHelper requestHelper, Context mContext) {
        return new ForumApi(requestHelper, userStorage, okHttpClient, mContext);
    }

    @Provides
    @Singleton
    public GameApi provideGameApi(RequestHelper requestHelper,
                                  @Named("api") OkHttpClient okHttpClient) {
        return new GameApi(requestHelper, okHttpClient);
    }

    @Provides
    @Singleton
    public CookieApi providesCookieApi(@Named("api") OkHttpClient okHttpClient) {
        return new CookieApi(okHttpClient);
    }
}
