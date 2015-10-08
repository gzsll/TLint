package com.gzsll.hupu.service.annotation;

import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ServiceClassInfo {
    public Class<?> cls = null;
    //action_to_methods map
    public HashMap<String, Method> methods = new HashMap<String, Method>();


    public void parseMethods() {
        System.out.println("IntentServiceClassInfo.parseMethods: " + cls.getName());

        for (Method method : cls.getMethods()) {
            //检索该类的函数列表
            if (method.isAnnotationPresent(ActionMethod.class)) {
                //对每一个ActionMethod记录保存
                ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
                validMethod(method, actionMethod);
                methods.put(actionMethod.value(), method);
            }
        }
    }

    //public int method_name(Intent intent, int flags, int startId)
    private void validMethod(Method method, ActionMethod actionMethod) {
        if (TextUtils.isEmpty(actionMethod.value())) {
            throw new RuntimeException("ActionMethod: " + method.getName() + "'s value(action string) should not be empty.");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length != 3
                || parameterTypes[0] != Intent.class
                || parameterTypes[1] != int.class
                || parameterTypes[2] != int.class
                ) {

            throw new RuntimeException("ActionMethod: " + method.getName() + " should like public int method_name(Intent intent, int flags, int startId).");
        }
    }
}
