package com.gzsll.hupu.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by sll on 2016/3/29.
 */
public class ShareUtil {

    public static void shareImage(Context mContext, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        mContext.startActivity(Intent.createChooser(shareIntent, title));
    }

    public static void share(Context mContext, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        Intent sendIntent = Intent.createChooser(intent, "分享");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(sendIntent);
    }
}
