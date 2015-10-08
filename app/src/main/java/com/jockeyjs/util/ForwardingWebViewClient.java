package com.jockeyjs.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class ForwardingWebViewClient extends WebViewClient {

    protected abstract WebViewClient delegate();

    protected boolean hasDelegate() {
        return delegate() != null;
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        if (hasDelegate())
            delegate().doUpdateVisitedHistory(view, url, isReload);
        else
            super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {
        if (hasDelegate())
            delegate().onFormResubmission(view, dontResend, resend);
        else
            super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (hasDelegate())
            delegate().onLoadResource(view, url);
        else
            super.onLoadResource(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if (hasDelegate())
            delegate().onPageFinished(view, url);
        else
            super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        if (hasDelegate())
            delegate().onPageStarted(view, url, favicon);
        else
            super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        if (hasDelegate())
            delegate()
                    .onReceivedError(view, errorCode, description, failingUrl);
        else
            super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {

        if (hasDelegate())
            delegate().onReceivedHttpAuthRequest(view, handler, host, realm);
        else
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void onReceivedLoginRequest(WebView view, String realm,
                                       String account, String args) {
        if (hasDelegate())
            delegate().onReceivedLoginRequest(view, realm, account, args);
        else
            super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        if (hasDelegate())
            delegate().onReceivedSslError(view, handler, error);
        else
            super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {

        if (hasDelegate())
            delegate().onUnhandledKeyEvent(view, event);
        else
            super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {

        if (hasDelegate())
            delegate().onScaleChanged(view, oldScale, newScale);
        else

            super.onScaleChanged(view, oldScale, newScale);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (hasDelegate())
            return delegate().shouldInterceptRequest(view, url);
        else
            return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (hasDelegate())
            return delegate().shouldOverrideKeyEvent(view, event);
        else
            return super.shouldOverrideKeyEvent(view, event);
    }

}
