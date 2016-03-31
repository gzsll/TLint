package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.widget.HuPuWebView;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class BrowserFragment extends BaseFragment {

    private Logger logger = Logger.getLogger(BrowserFragment.class.getSimpleName());

    public static BrowserFragment newInstance(String url, String title) {
        BrowserFragment mFragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Bind(R.id.webView)
    HuPuWebView webView;
    @Bind(R.id.progress)
    ProgressBar progress;

    @Inject
    Activity mActivity;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    private String url;
    private String title;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_browser;
    }

    @Override
    public void getBundle(Bundle bundle) {
        url = bundle.getString("url");
        title = bundle.getString("title");
        if (mSettingPrefHelper.getNightModel()) {
            url += "&night=1";
        }
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        showContent(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(BrowserFragment.this.title)) {
                    mActivity.setTitle(BrowserFragment.this.title);
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

    }

    @Override
    public void initData() {
        webView.loadUrl(url);
    }

    public void reload() {
        webView.reload();
    }


}
