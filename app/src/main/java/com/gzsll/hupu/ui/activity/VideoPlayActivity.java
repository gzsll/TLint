package com.gzsll.hupu.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.InvocationTargetException;


@EActivity(R.layout.activity_video)
public class VideoPlayActivity extends Activity {
    PowerManager powerManager = null;
    WakeLock wakeLock = null;
    @Extra
    String url;
    private View myView = null;
    private ProgressDialog progressDialog;
    @ViewById
    WebView mWebView;


    private WebViewClient mWebViewClient = new WebViewClient() {
        // 处理页面导航
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!url.contains("iqiyi")) {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(
                            VideoPlayActivity.this, "",
                            "正在加载中。。。", false);
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }
    };

    // 浏览网页历史记录
    // goBack()和goForward()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // if(mWebView.canGoBack()){
            // mWebView.goBack();
            mChromeClient.onHideCustomView();
            // }

            // else{
            // finish();}
            // return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient mChromeClient = new WebChromeClient() {

        private CustomViewCallback myCallback = null;

        // 配置权限 （在WebChromeClinet中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        // 扩充数据库的容量（在WebChromeClinet中实现）
        @Override
        public void onExceededDatabaseQuota(String url,
                                            String databaseIdentifier, long currentQuota,
                                            long estimatedSize, long totalUsedQuota,
                                            WebStorage.QuotaUpdater quotaUpdater) {

            quotaUpdater.updateQuota(estimatedSize * 2);
        }

        // 扩充缓存的容量
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded,
                                             long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(spaceNeeded * 2);
        }

        // Android 使WebView支持HTML5 Video（全屏）播放的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }

            ViewGroup parent = (ViewGroup) mWebView.getParent();
            parent.removeView(mWebView);
            parent.addView(view);
            myView = view;
            myCallback = callback;
            mChromeClient = this;
        }

        @Override
        public void onHideCustomView() {
            if (myView != null) {
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }

                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                parent.addView(mWebView);
                myView = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // context = this.getApplicationContext();
        this.powerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK, "My Lock");

        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        String appCacheDir = this.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setAppCachePath(appCacheDir);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
        mWebView.getSettings().setAllowFileAccess(true);
        //	mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(url);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause")
                    .invoke(mWebView, (Object[]) null);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mChromeClient.onHideCustomView();
        this.wakeLock.acquire();
        mWebView.onPause();
        mWebView.pauseTimers();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.wakeLock.acquire();
        try {
            mWebView.getClass().getMethod("onResume")
                    .invoke(mWebView, (Object[]) null);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // mChromeClient.
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.destroy();
    }

}
