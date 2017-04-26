package com.gzsll.hupu.util;

import android.content.Context;

/**
 * Created by sll on 2016/3/30.
 */
public class StringUtil {

    public static void copy(Context mContext, String stripped) {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("content", stripped);
        clipboard.setPrimaryClip(clip);
        ToastUtil.showToast("复制成功");
    }
}
