package com.gzsll.hupu.injector.component;

import android.content.Context;

import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.api.login.CookieApi;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.helper.CacheHelper;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.DataCleanHelper;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.FormatHelper;
import com.gzsll.hupu.helper.NetWorkHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.helper.SecurityHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.helper.ShareHelper;
import com.gzsll.hupu.helper.StringHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.injector.module.ApiModule;
import com.gzsll.hupu.injector.module.ApplicationModule;
import com.gzsll.hupu.injector.module.DBModule;
import com.gzsll.hupu.injector.module.HelperModule;
import com.gzsll.hupu.widget.HuPuWebView;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by sll on 2016/3/8.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, DBModule.class, HelperModule.class})
public interface ApplicationComponent {

    Context getContext();

    Bus getBus();


    ForumApi getForumApi();

    GameApi getGameApi();

    CookieApi getCookieApi();

    UserDao getUserDao();

    ForumDao getForumDao();

    NetWorkHelper getNetWorkHelper();



    SecurityHelper getSecurityHelper();

    FileHelper getFileHelper();

    ConfigHelper getConfigHelper();

    OkHttpHelper getOkHttpHelper();

    UserStorage getUserStorage();

    ResourceHelper getResourceHelper();

    ToastHelper getToastHelper();


    SettingPrefHelper getSettingPrefHelper();

    RequestHelper getRequestHelper();

    CacheHelper getCacheHelper();

    DataCleanHelper getDataCleanHelper();

    FormatHelper getFormatHelper();

    OkHttpClient getOkHttpClient();

    ShareHelper getShareHelper();

    StringHelper getStringHelper();


    void inject(MyApplication mApplication);

    void inject(HuPuWebView mHupuWebView);
}
