package com.gzsll.hupu.service.annotation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

//未测试，使用时注意调试。
public class AnnotationService extends Service {
    private static HashMap<Class<?>, ServiceClassInfo> cls_map = new HashMap<Class<?>, ServiceClassInfo>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = getAction(intent);
        ServiceClassInfo cls_info = cls_map.get(this.getClass());
        if (cls_info == null) {
            cls_info = new ServiceClassInfo();
            cls_info.cls = this.getClass();
            cls_info.parseMethods();
            cls_map.put(cls_info.cls, cls_info);
        }
        Method method = cls_info.methods.get(action);
        if (method != null) {
            try {
                return (Integer) method.invoke(this, new Object[]{intent, flags, startId});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultHandleIntent(intent, flags, startId);
    }

    protected int defaultHandleIntent(Intent intent, int flags, int startId) {
        Log.d("AnnotationService", "intent: " + intent + " is handled by default.");
        return super.onStartCommand(intent, flags, startId);
    }

    protected String getAction(Intent intent) {
        if (intent == null) {
            return "";
        }

        String action = intent.getAction();
        if (action == null) {
            action = "";
        }

        return action;
    }
}
