package com.gzsll.hupu.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.UserStorage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import javax.inject.Inject;

/**
 * Created by gzsll on 2014/9/14 0014.
 */
@EActivity(R.layout.activity_browser)
public class BrowserActivity extends BaseSwipeBackActivity {
    Logger logger = Logger.getLogger("BrowserActivity");
    @ViewById
    WebView webView;
    @ViewById
    ProgressBar progress;
    @ViewById
    Toolbar toolbar;
    @Extra
    String url;

    @Inject
    UserStorage mUserStorage;


    @AfterViews
    void init() {
        initToolBar(toolbar);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().toString());
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeGroup(R.id.browser);
        getMenuInflater().inflate(R.menu.menu_browser, menu);

        String shareContent = String.format("%s %s ", webView.getTitle() + "", webView.getUrl() + "");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);


        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider shareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareProvider.setShareHistoryFileName("channe_share.xml");
        shareProvider.setShareIntent(shareIntent);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            webView.reload();
        } else if (item.getItemId() == R.id.copy) {
            copyToClipboard(webView.getUrl());
            showToast("链接已复制");
        } else if (item.getItemId() == R.id.to_browser) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(webView.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            } catch (Exception e) {
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void copyToClipboard(String text) {
        // 得到剪贴板管理器
        try {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, text.trim()));
        } catch (Exception e) {
        }
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
