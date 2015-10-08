package com.gzsll.hupu.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gzsll on 2014/9/23 0023.
 */
public class RequestHelper {
    Logger logger = Logger.getLogger("RequestUtil");

    private SecurityHelper securityHelper;
    private Context context;

    public RequestHelper(SecurityHelper securityHelper, Context context) {
        this.securityHelper = securityHelper;
        this.context = context;
    }


    public Map<String, String> getHttpRequestMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", getDeviceId());
        map.put("v", "7.0");
        //  map.put("mode", "0");
        //   map.put("timestamp", String.valueOf(System.currentTimeMillis()).substring(0, 10));
        map.put("platform", "android");
        map.put("version", "1.1");
        return map;
    }

    public String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getDeviceId() {
        String deviceId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() == null) {
            deviceId = getAndroidId();
        } else {
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }


    public String getUrl(Map<String, String> map) {
        String getUrl = "?sign=" + getRequestSign(map);
        StringBuilder result = new StringBuilder(getUrl);
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        return result.toString();
    }


    public String getRequestSign(Map<String, String> map) {
        ArrayList<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i = i + 1) {
            Map.Entry<String, String> map1 = list.get(i);
            builder.append(map1.getKey()).append(map1.getValue());
        }
        builder.append("3542e676b4c80983f6131cdfe577ac9b");
        logger.debug("builder:" + builder.toString());
        return securityHelper.getMD5(builder.toString());
    }


}
