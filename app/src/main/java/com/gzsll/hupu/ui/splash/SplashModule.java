package com.gzsll.hupu.ui.splash;

import dagger.Module;

/**
 * Created by sll on 2016/5/13.
 */
@Module
public class SplashModule {

    private SplashActivity mActivity;

    public SplashModule(SplashActivity mActivity) {
        this.mActivity = mActivity;
    }
}
