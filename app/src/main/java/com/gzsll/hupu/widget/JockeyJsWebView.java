package com.gzsll.hupu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jockeyjs.Jockey;
import com.jockeyjs.JockeyAsyncHandler;
import com.jockeyjs.JockeyCallback;
import com.jockeyjs.JockeyImpl;

import org.apache.log4j.Logger;

import java.net.URI;
import java.util.Map;

/**
 * Created by sll on 2015/6/17.
 */
public class JockeyJsWebView extends WebView {
    private Jockey jockey;
    private JockeyAsyncHandler jockeyAsyncHandler;
    private H5Callback callback;

    Logger logger = Logger.getLogger(JockeyJsWebView.class.getSimpleName());


    public JockeyJsWebView(Context context) {
        this(context, null);
    }

    public JockeyJsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setSupportMultipleWindows(true);
        settings.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT > 12) {
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
        }
        settings.setAppCacheEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(2);
        if (Build.VERSION.SDK_INT > 11) {
            setLayerType(0, null);
        }
    }


    public void initJockey() {
        jockeyAsyncHandler = new JockeyAsyncHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                if (callback != null) {
                    callback.doPerform(payload);
                }
            }
        };

        if (jockey == null) {
            jockey = JockeyImpl.getDefault();
        }
        jockey.configure(this);
        jockey.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (!isJockeyScheme(new URI(url))) {
                        if (callback != null) {
                            callback.openBrowser(url);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (callback != null) {
                    callback.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (callback != null) {
                    callback.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (callback != null) {
                    callback.onReceivedError(view, errorCode, description, failingUrl);
                }
            }
        });
        setJockeyEvents();
        setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
                return super.onJsAlert(webView, str, str2, jsResult);
            }

            public boolean onJsTimeout() {
                return callback != null ? callback.onJsTimeout() : super.onJsTimeout();
            }
        });


    }

    public boolean isJockeyScheme(URI uri) {
        return uri.getScheme().equals("jockey") && !uri.getQuery().equals("");
    }


    protected void setJockeyEvents() {
        if (this.callback != null) {
            this.callback.setJockeyEvents();
        }
    }

    public void onJSEvent(String str) {
        onJSEvent(str, jockeyAsyncHandler);
    }

    public void onJSEvent(String str, JockeyAsyncHandler jockeyAsyncHandler) {
        if (jockey != null) {
            jockey.on(str, jockeyAsyncHandler);
        }
    }

    public void sendMessageToJS(String str, Map<Object, Object> map, JockeyCallback jockeyCallback) {
        if (jockey != null && jockeyCallback != null) {
            jockey.send(str, this, map, jockeyCallback);
        } else if (jockey != null) {
            jockey.send(str, this, map);
        }
    }

    public void setWebViewHeight(int i) {
        try {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = convertHeight(i);
            setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private int convertHeight(float dpValue) {
        return (int) ((((float) (dpValue >= 0.0f ? 1 : -1)) * 0.5f) + (dpValue * getContext().getResources().getDisplayMetrics().density));
    }


    public void setCallback(H5Callback callback) {
        this.callback = callback;
    }

    @Override
    public void destroy() {
        jockey.configure(null);
        setWebChromeClient(null);
        setWebViewClient(null);
        super.destroy();
    }
}
