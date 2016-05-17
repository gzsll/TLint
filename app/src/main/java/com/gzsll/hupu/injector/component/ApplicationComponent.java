package com.gzsll.hupu.injector.component;

import android.content.Context;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.api.login.CookieApi;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.components.retrofit.RequestHelper;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.injector.module.ApiModule;
import com.gzsll.hupu.injector.module.ApplicationModule;
import com.gzsll.hupu.injector.module.DBModule;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.widget.HuPuWebView;
import com.squareup.otto.Bus;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by sll on 2016/3/8.
 */
@Singleton @Component(modules = { ApplicationModule.class, ApiModule.class, DBModule.class })
public interface ApplicationComponent {

  Context getContext();

  Bus getBus();

  ForumApi getForumApi();

  GameApi getGameApi();

  CookieApi getCookieApi();

  UserDao getUserDao();

  ForumDao getForumDao();

  OkHttpHelper getOkHttpHelper();

  RequestHelper getRequestHelper();

  UserStorage getUserStorage();

  void inject(MyApplication mApplication);

  void inject(BaseActivity mBaseActivity);

  void inject(HuPuWebView mHupuWebView);
}
