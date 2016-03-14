package com.gzsll.hupu.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sll on 2016/1/11.
 */
public class ToastHelper {

    private Context mContext;


    public ToastHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void showToast(int resId) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
