/*******************************************************************************
 * Copyright (c) 2013,  Paul Daniels
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.gzsll.hupu.components.jockeyjs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JockeyService extends Service implements Jockey {

    private final IBinder _binder = new JockeyBinder();

    private Jockey _jockeyImpl = JockeyImpl.getDefault();

    /**
     * Convenience method for binding to the JockeyService
     */
    public static boolean bind(Context context, ServiceConnection connection) {
        return context.bindService(new Intent(context, JockeyService.class), connection,
                Context.BIND_AUTO_CREATE);
    }

    public static void unbind(Context context, ServiceConnection connection) {
        context.unbindService(connection);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return _binder;
    }

    public class JockeyBinder extends Binder {

        public Jockey getService() {
            return JockeyService.this;
        }
    }

    @Override
    public void setOnValidateListener(OnValidateListener listener) {
        _jockeyImpl.setOnValidateListener(listener);
    }

    public void on(String type, JockeyHandler... handler) {
        _jockeyImpl.on(type, handler);
    }

    @Override
    public void off(String type) {
        _jockeyImpl.off(type);
    }

    public void send(String type, WebView toWebView) {
        send(type, toWebView, null);
    }

    public void send(String type, WebView toWebView, Object withPayload) {
        send(type, toWebView, withPayload, null);
    }

    public void send(String type, WebView toWebView, JockeyCallback complete) {
        send(type, toWebView, null, complete);
    }

    public void send(String type, WebView toWebView, Object withPayload, JockeyCallback complete) {
        _jockeyImpl.send(type, toWebView, withPayload, complete);
    }

    public void triggerCallbackOnWebView(WebView webView, int messageId) {
        _jockeyImpl.triggerCallbackOnWebView(webView, messageId);
    }

    public void configure(WebView webView) {
        _jockeyImpl.configure(webView);
    }

    @Override
    public boolean handles(String eventName) {
        return _jockeyImpl.handles(eventName);
    }

    public void setWebViewClient(WebViewClient client) {
        _jockeyImpl.setWebViewClient(client);
    }
}
