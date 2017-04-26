package com.gzsll.hupu.injector.component;

import android.app.NotificationManager;
import android.content.Context;

import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.ImageCacheDao;
import com.gzsll.hupu.db.ReadThreadDao;
import com.gzsll.hupu.db.ThreadDao;
import com.gzsll.hupu.db.ThreadInfoDao;
import com.gzsll.hupu.db.ThreadReplyDao;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.injector.module.ApiModule;
import com.gzsll.hupu.injector.module.ApplicationModule;
import com.gzsll.hupu.injector.module.DBModule;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.widget.HuPuWebView;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sll on 2016/3/8.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class, ApiModule.class, DBModule.class
})
public interface ApplicationComponent {

    Context getContext();

    Bus getBus();

    ForumApi getForumApi();

    GameApi getGameApi();


    UserDao getUserDao();

    ForumDao getForumDao();

    ThreadDao getThreadDao();

    ThreadInfoDao getThreadInfoDao();

    ThreadReplyDao getThreadReplyDao();

    ReadThreadDao getReadThreadDao();

    ImageCacheDao getImageCacheDao();

    OkHttpHelper getOkHttpHelper();


    UserStorage getUserStorage();

    NotificationManager getNotificationManager();


    void inject(MyApplication mApplication);

    void inject(BaseActivity mBaseActivity);

    void inject(HuPuWebView mHupuWebView);
}
