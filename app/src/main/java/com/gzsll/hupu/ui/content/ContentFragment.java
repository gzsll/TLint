package com.gzsll.hupu.ui.content;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.ImagePreview;
import com.gzsll.hupu.components.jockeyjs.JockeyHandler;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.imagepreview.ImagePreviewActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.ui.thread.list.ThreadListActivity;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;
import com.gzsll.hupu.util.SettingPrefUtil;
import com.gzsll.hupu.widget.H5Callback;
import com.gzsll.hupu.widget.JockeyJsWebView;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentFragment extends BaseFragment
        implements ContentPagerContract.View, H5Callback, JockeyJsWebView.OnScrollChangedCallback {

    public static ContentFragment newInstance(String fid, String tid, String pid, int page) {
        ContentFragment mFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("tid", tid);
        bundle.putString("pid", pid);
        bundle.putInt("page", page);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @BindView(R.id.webView)
    JockeyJsWebView webView;

    @Inject
    ContentPagerPresenter mContentPresenter;

    private String tid;
    private String fid;
    private String pid;
    private int page;

    @Override
    public void initInjector() {
        getComponent(ContentComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_content;
    }

    @Override
    public void getBundle(Bundle bundle) {
        tid = bundle.getString("tid");
        fid = bundle.getString("fid");
        pid = bundle.getString("pid");
        page = bundle.getInt("page");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mContentPresenter.attachView(this);
        webView.setCallback(this);
        webView.initJockey();
        webView.setOnScrollChangedCallback(this);
        webView.addJavascriptInterface(mContentPresenter.getJavaScriptInterface(), "HuPuBridge");
    }

    @Override
    public void initData() {
        webView.loadUrl(SettingPrefUtil.getNightModel(getActivity())
                ? "file:///android_asset/hupu_thread_night.html"
                : "file:///android_asset/hupu_thread.html");
    }

    @Override
    public void onReloadClicked() {
        mContentPresenter.onReload();
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
    public void showLoading() {
        showProgress(true);
    }

    @Override
    public void hideLoading() {
        showContent(true);
    }

    @Override
    public void onError() {
        setEmptyText("数据加载失败");
        showError(true);
    }

    @Override
    public void sendMessageToJS(String handlerName, Object object) {
        webView.sendMessageToJS(handlerName, object);
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void showReplyUi(String fid, String tid, String pid, String title) {
        PostActivity.startActivity(getActivity(), Constants.TYPE_REPLY, fid, tid, pid, title);
    }

    @Override
    public void showReportUi(String tid, String pid) {
        ReportActivity.startActivity(getActivity(), tid, pid);
    }

    @Override
    public void showBrowserUi(String url) {
        BrowserActivity.startActivity(getActivity(), url);
    }

    @Override
    public void showContentUi(String tid, String pid, int page) {
        ContentActivity.startActivity(getActivity(), "", fid, pid, page);
    }

    @Override
    public void showThreadListUi(String fid) {
        ThreadListActivity.startActivity(getActivity(), fid);
    }

    @Override
    public void showUserProfileUi(String uid) {
        UserProfileActivity.startActivity(getActivity(), uid);
    }

    @Override
    public void showLoginUi() {
        LoginActivity.startActivity(getActivity());
    }

    @Override
    public void onClose() {
        getActivity().finish();
    }

    @Override
    public void doPerform(Map<Object, Object> map) {

    }

    @Override
    public void onPageFinished(WebView webView, String str) {
        mContentPresenter.onThreadInfoReceive(tid, fid, pid, page);
    }

    @Override
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    @Override
    public void setJockeyEvents() {
        webView.onJSEvent("showImg", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                ImagePreview preview = JSON.parseObject(JSON.toJSONString(payload), ImagePreview.class);
                ImagePreviewActivity.startActivity(getActivity(), preview.imgs.get(preview.index),
                        preview.imgs);
            }
        });
        webView.onJSEvent("showUrl", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                mContentPresenter.handlerUrl(((String) payload.get("url")));
            }
        });
        webView.onJSEvent("showUser", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                UserProfileActivity.startActivity(getActivity(), ((String) payload.get("uid")));
            }
        });
        webView.onJSEvent("showMenu", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                int area = Integer.valueOf((String) payload.get("area"));
                int index = Integer.valueOf((String) payload.get("index"));
                String type = (String) payload.get("type");
                switch (type) {
                    case "light":
                        mContentPresenter.addLight(area, index);
                        break;
                    case "rulight":
                        mContentPresenter.addRuLight(area, index);
                        break;
                    case "reply":
                        mContentPresenter.onReply(area, index);
                        break;
                    case "report":
                        mContentPresenter.onReport(area, index);
                        break;
                }
            }
        });
    }

    @Override
    public void openBrowser(String url) {
        mContentPresenter.handlerUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentPresenter.detachView();
        if (webView != null) {
            webView.destroy();
        }
    }
}
