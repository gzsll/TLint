package com.gzsll.hupu.components.jockeyjs;

import android.webkit.WebView;

import com.alibaba.fastjson.JSON;

public class DefaultJockeyImpl extends JockeyImpl {

    private int messageCount = 0;

    @Override
    public void send(String type, WebView toWebView, Object withPayload, JockeyCallback complete) {
        int messageId = messageCount;

        if (complete != null) {
            add(messageId, complete);
        }

        if (withPayload != null) {
            withPayload = JSON.toJSON(withPayload);
        }

        String url =
                String.format("javascript:Jockey.trigger(\"%s\", %d, %s)", type, messageId, withPayload);
        toWebView.loadUrl(url);

        ++messageCount;
    }

    @Override
    public void triggerCallbackOnWebView(WebView webView, int messageId) {
        String url = String.format("javascript:Jockey.triggerCallback(\"%d\")", messageId);
        webView.loadUrl(url);
    }
}
