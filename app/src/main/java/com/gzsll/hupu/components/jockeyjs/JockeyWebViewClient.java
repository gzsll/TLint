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

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.gzsll.hupu.components.jockeyjs.util.ForwardingWebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressLint("SetJavaScriptEnabled")
class JockeyWebViewClient extends ForwardingWebViewClient {

    private JockeyImpl _jockeyImpl;
    private WebViewClient _delegate;

    public JockeyWebViewClient(JockeyImpl jockey) {
        _jockeyImpl = jockey;
    }

    public JockeyImpl getImplementation() {
        return _jockeyImpl;
    }

    protected void setDelegate(WebViewClient client) {
        _delegate = client;
    }

    public WebViewClient delegate() {
        return _delegate;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (delegate() != null && delegate().shouldOverrideUrlLoading(view, url)) return true;

        try {
            URI uri = new URI(url);

            if (isJockeyScheme(uri)) {
                processUri(view, uri);
                return true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HostValidationException e) {
            e.printStackTrace();
            Log.e("Jockey", "The source of the event could not be validated!");
        }
        return false;
    }

    public boolean isJockeyScheme(URI uri) {
        return uri.getScheme().equals("jockey") && !uri.getQuery().equals("");
    }

    public void processUri(WebView view, URI uri) throws HostValidationException {
        String[] parts = uri.getPath().replaceAll("^\\/", "").split("/");
        String host = uri.getHost();

        JockeyWebViewPayload payload =
                checkPayload(JSON.parseObject(uri.getQuery(), JockeyWebViewPayload.class));

        if (parts.length > 0) {
            if (host.equals("event")) {
                getImplementation().triggerEventFromWebView(view, payload);
            } else if (host.equals("callback")) {
                getImplementation().triggerCallbackForMessage(Integer.parseInt(parts[0]));
            }
        }
    }

    public JockeyWebViewPayload checkPayload(JockeyWebViewPayload fromJson)
            throws HostValidationException {
        validateHost(fromJson.host);
        return fromJson;
    }

    private void validateHost(String host) throws HostValidationException {
        getImplementation().validate(host);
    }
}