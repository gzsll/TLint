package com.gzsll.hupu.helper;

import android.content.Context;

/**
 * Created by sll on 2016/3/30.
 */
public class StringHelper {

    private Context mContext;
    private ToastHelper mToastHelper;

    public StringHelper(Context mContext, ToastHelper mToastHelper) {
        this.mContext = mContext;
        this.mToastHelper = mToastHelper;
    }


    public void copy(String stripped) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(stripped);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("content", stripped);
            clipboard.setPrimaryClip(clip);
        }
        mToastHelper.showToast("复制成功");
    }
}
