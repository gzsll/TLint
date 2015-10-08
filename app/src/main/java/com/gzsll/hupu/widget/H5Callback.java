package com.gzsll.hupu.widget;

import android.graphics.Bitmap;
import android.webkit.WebView;

import java.util.Map;

/**
 * Created by admin on 2015/6/17.
 */
public interface H5Callback {
    void doPerform(Map<Object, Object> map);

    boolean onJsTimeout();

    void onPageFinished(WebView webView, String str);

    void onPageStarted(WebView webView, String str, Bitmap bitmap);

    void onReceivedError(WebView webView, int i, String str, String str2);

    void setJockeyEvents();

    void openBrowser(String url);
}