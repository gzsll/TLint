package com.gzsll.hupu.ui.splash;

import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.UpdateAgent;
import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2016/5/13.
 */
@Module public class SplashModule {

  private SplashActivity mActivity;

  public SplashModule(SplashActivity mActivity) {
    this.mActivity = mActivity;
  }

  @Provides @PerActivity UpdateAgent provideUpdateAgent(OkHttpHelper mOkHttpHelper) {
    return new UpdateAgent(mOkHttpHelper, mActivity);
  }
}
