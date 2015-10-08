package com.gzsll.hupu.service.annotation;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * !!不要使用ESService
 * 会造成在2.x下出现问题。NoSuchMethod
 */
public class IntentAnnotationService extends IntentService {

    public static final Logger logger = Logger.getLogger(IntentAnnotationService.class.getSimpleName());

    public static final String TAG = "IntentAnnotationService";

    private static HashMap<Class<?>, IntentServiceClassInfo> cls_map = new HashMap<Class<?>, IntentServiceClassInfo>();

    public IntentAnnotationService() {
        super(IntentAnnotationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = getAction(intent);
        IntentServiceClassInfo cls_info = cls_map.get(this.getClass());
        if (cls_info == null) {
            cls_info = new IntentServiceClassInfo();
            cls_info.cls = this.getClass();
            cls_info.parseMethods();
            cls_map.put(cls_info.cls, cls_info);
        }
        Method method = cls_info.methods.get(action);
        if (method == null) {
            defaultHandleIntent(intent);
        } else {
            long start = System.currentTimeMillis();
            try {
                logger.debug(cls_info.cls.getSimpleName() + "#onHandleIntent: " + action + ", " + method.getName());
                method.invoke(this, new Object[]{intent});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                long end = System.currentTimeMillis();
                logger.debug(
                        String.format("%s.%s [%d ms]",
                                cls_info.cls.getSimpleName(),
                                method.getName(),
                                (end - start)));
            }
        }
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

    protected void defaultHandleIntent(Intent intent) {
        Log.d("IntentAnnotationService", "intent: " + intent + " is handled by default.");
    }
}
