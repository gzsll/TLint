package com.gzsll.hupu.ui.browser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.widget.HuPuWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class BrowserFragment extends BaseFragment {


    public static BrowserFragment newInstance(String url, String title) {
        return newInstance(url, title, false);
    }

    public static BrowserFragment newInstance(String url, String title, boolean external) {
        BrowserFragment mFragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        bundle.putBoolean("external", external);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @BindView(R.id.webView)
    HuPuWebView webView;
    @BindView(R.id.progress)
    ProgressBar progress;

    private String url;
    private String title;
    private boolean external;

    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_browser;
    }

    @Override
    public void getBundle(Bundle bundle) {
        url = bundle.getString("url");
        title = bundle.getString("title");
        external = bundle.getBoolean("external");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        showContent(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(BrowserFragment.this.title)) {
                    getActivity().setTitle(BrowserFragment.this.title);
                }
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
        if (external) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null) view.loadUrl(url);
                    return true;
                }
            });
        }
    }

    @Override
    public void initData() {
        webView.loadUrl(url);
    }

    public void reload() {
        webView.reload();
    }
}
