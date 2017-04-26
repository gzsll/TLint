package com.gzsll.hupu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.components.jockeyjs.Jockey;
import com.gzsll.hupu.components.jockeyjs.JockeyAsyncHandler;
import com.gzsll.hupu.components.jockeyjs.JockeyCallback;
import com.gzsll.hupu.components.jockeyjs.JockeyHandler;
import com.gzsll.hupu.components.jockeyjs.JockeyImpl;
import com.gzsll.hupu.util.NetWorkUtil;

import java.net.URI;
import java.util.Map;

/**
 * Created by sll on 2015/6/17.
 */
public class JockeyJsWebView extends WebView {
    private Jockey jockey;
    private JockeyAsyncHandler jockeyAsyncHandler;
    private H5Callback callback;


    public JockeyJsWebView(Context context) {
        this(context, null);
    }

    public JockeyJsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
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
        settings.setCacheMode(NetWorkUtil.isNetworkConnected(getContext()) ? WebSettings.LOAD_DEFAULT
                : WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (callback != null) {
                    callback.onReceivedError(view, request, error);
                }
            }
        });
        setJockeyEvents();
    }

    public boolean isJockeyScheme(URI uri) {
        return uri.getScheme().equals("jockey") && !uri.getQuery().equals("");
    }

    protected void setJockeyEvents() {
        if (callback != null) {
            callback.setJockeyEvents();
        }
    }

    public void onJSEvent(String str, JockeyAsyncHandler jockeyAsyncHandler) {
        if (jockey != null) {
            jockey.on(str, jockeyAsyncHandler);
        }
    }

    public void onJSEvent(String str, JockeyHandler handler) {
        if (jockey != null) {
            jockey.on(str, handler);
        }
    }

    public void sendMessageToJS(String str, Object object) {
        if (jockey != null) {
            jockey.send(str, this, object);
        }
    }

    public void sendMessageToJS(String str, Object object, JockeyCallback jockeyCallback) {
        if (jockey != null && jockeyCallback != null) {
            jockey.send(str, this, object, jockeyCallback);
        }
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

    private OnScrollChangedCallback mOnScrollChangedCallback;

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }
}
