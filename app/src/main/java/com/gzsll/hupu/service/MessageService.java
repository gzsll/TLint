package com.gzsll.hupu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.injector.component.DaggerServiceComponent;
import com.gzsll.hupu.injector.module.ServiceModule;

import org.apache.log4j.Logger;

/**
 * Created by sll on 2016/3/16.
 */
public class MessageService extends Service {

    Logger logger = Logger.getLogger(MessageService.class.getSimpleName());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("服务初始化");
        DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }
}
