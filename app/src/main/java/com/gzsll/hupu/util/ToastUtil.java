package com.gzsll.hupu.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sll on 2016/1/11.
 */
public class ToastUtil {

    public static Context mContext;

    private ToastUtil() {
    }

    public static void register(Context context) {
        mContext = context.getApplicationContext();
    }

    public static void showToast(int resId) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
