package com.gzsll.hupu.ui.content;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.widget.HuPuWebView;

import org.apache.log4j.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentFragment extends BaseFragment implements HuPuWebView.HuPuWebViewCallBack, HuPuWebView.OnScrollChangedCallback {

    private Logger logger = Logger.getLogger(ContentFragment.class.getSimpleName());


    public static ContentFragment newInstance(String url) {
        ContentFragment mFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Bind(R.id.webView)
    HuPuWebView webView;

    private String url;


    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_content;
    }

    @Override
    public void getBundle(Bundle bundle) {
        url = bundle.getString("url");
        logger.debug("url:" + url);
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        webView.setCallBack(this);
        webView.setOnScrollChangedCallback(this);
    }


    @Override
    public void initData() {
        showProgress(true);
        webView.loadUrl(url);
    }


    @Override
    public void onFinish() {
        showContent(true);
    }

    @Override
    public void onUpdatePager(int page, int total) {

    }

    @Override
    public void onError() {
        showError(true);
    }

    @Override
    public void onReloadClicked() {
        super.onReloadClicked();
        initData();
    }

    @Override
    public void onScroll(int dx, int dy) {
        if (Math.abs(dy) > 4) {
            ContentActivity activity = ((ContentActivity) getActivity());
            if (activity != null) {
                activity.setFloatingMenuVisibility(dy < 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            if (Build.VERSION.SDK_INT > 17) {
                String str = "about:blank";
                webView.loadUrl(str);
            } else {
                webView.clearView();
            }
        }
        super.onDestroyView();
    }

}
