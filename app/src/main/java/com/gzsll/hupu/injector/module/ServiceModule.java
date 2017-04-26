package com.gzsll.hupu.injector.module;

import android.app.Service;

import com.gzsll.hupu.injector.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 16/03/16.
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    public Service provideContext() {
        return mService;
    }
}
