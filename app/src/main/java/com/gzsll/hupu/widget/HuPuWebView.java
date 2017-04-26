package com.gzsll.hupu.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.components.retrofit.RequestHelper;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.content.ContentActivity;
import com.gzsll.hupu.ui.imagepreview.ImagePreviewActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.ui.thread.list.ThreadListActivity;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;
import com.gzsll.hupu.util.StringUtil;
import com.gzsll.hupu.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/10.
 */
public class HuPuWebView extends WebView {


    private String basicUA;
    private Map<String, String> header;

    @Inject
    UserStorage mUserStorage;
    @Inject
    RequestHelper mRequestHelper;

    public HuPuWebView(Context context) {
        super(context);
        init();
    }

    public HuPuWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCallBack(HuPuWebViewCallBack callBack) {
        this.callBack = callBack;
    }


    private void init() {
        ((MyApplication) getContext().getApplicationContext()).getApplicationComponent().inject(this);
        WebSettings settings = getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(1);
        settings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT > 6) {
            settings.setAppCacheEnabled(true);
            settings.setLoadWithOverviewMode(true);
        }
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        String path = getContext().getFilesDir().getPath();
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(path);
        settings.setDomStorageEnabled(true);
        this.basicUA = settings.getUserAgentString() + " kanqiu/7.05.6303/7059";
        setBackgroundColor(0);
        initWebViewClient();
        try {
            if (mUserStorage.isLogin()) {
                String token = mUserStorage.getToken();
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setCookie("http://bbs.mobileapi.hupu.com",
                        "u=" + URLEncoder.encode(mUserStorage.getCookie(), "utf-8"));
                cookieManager.setCookie("http://bbs.mobileapi.hupu.com",
                        "_gamesu=" + URLEncoder.encode(token, "utf-8"));
                cookieManager.setCookie("http://bbs.mobileapi.hupu.com", "_inKanqiuApp=1");
                cookieManager.setCookie("http://bbs.mobileapi.hupu.com", "_kanqiu=1");
                CookieSyncManager.getInstance().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWebViewClient() {
        CookieManager.getInstance().setAcceptCookie(true);
        setWebViewClient(new HupuWebClient());
    }

    private class HupuWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (url.startsWith("hupu") || url.startsWith("kanqiu")) {
                if (scheme != null) {
                    handleScheme(scheme, url);
                }
            } else if (scheme.equals("http") || scheme.equals("https")) {
                BrowserActivity.startActivity(getContext(), url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (callBack != null) {
                callBack.onFinish();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (callBack != null) {
                callBack.onError();
            }
        }
    }

    private void handleScheme(String scheme, String url) {
        if (scheme != null) {
            if (scheme.equalsIgnoreCase("kanqiu")) {
                handleKanQiu(url);
            } else if (scheme.equalsIgnoreCase("browser")
                    || scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https")) {
                BrowserActivity.startActivity(getContext(), url);
            } else if (scheme.equalsIgnoreCase("hupu")) {
                try {
                    JSONObject object = new JSONObject(Uri.decode(url.substring("hupu".length() + 3)));
                    String method = object.optString("method");
                    String successcb = object.optString("successcb");
                    handleHuPu(method, object.getJSONObject("data"), successcb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleKanQiu(String url) {
        if (url.contains("topic")) {
            Uri uri = Uri.parse(url);
            String tid = uri.getLastPathSegment();
            String page = uri.getQueryParameter("page");
            String pid = uri.getQueryParameter("pid");
            ContentActivity.startActivity(getContext(), "", tid, pid,
                    TextUtils.isEmpty(page) ? 1 : Integer.valueOf(page));
        } else if (url.contains("board")) {
            String boardId = url.substring(url.lastIndexOf("/") + 1);
            ThreadListActivity.startActivity(getContext(), boardId);
        } else if (url.contains("people")) {
            String uid = url.substring(url.lastIndexOf("/") + 1);
            UserProfileActivity.startActivity(getContext(), uid);
        }
    }

    private void handleHuPu(String method, JSONObject data, String successcb) throws Exception {
        switch (method) {
            case "bridgeReady":
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("hybridVer", "1.0");
                    jSONObject.put("supportAjax", true);
                    jSONObject.put("appVer", "7.0.5.6303");
                    jSONObject.put("appName", "com.hupu.games");
                    jSONObject.put("lowDevice", false);
                    jSONObject.put("scheme", "hupu");
                    jSONObject.put("did", mRequestHelper.getDeviceId());
                    jSONObject.put("platform", "Android");
                    jSONObject.put("device", Build.PRODUCT);
                    jSONObject.put("osVer", Build.VERSION.RELEASE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String js = "javascript:HupuBridge._handle_('"
                        + successcb
                        + "','"
                        + jSONObject.toString()
                        + "','null','null');";
                loadUrl(js);
                break;
            case "hupu.ui.updatebbspager":
                int page = data.getInt("page");
                int total = data.getInt("total");
                if (callBack != null) {
                    callBack.onUpdatePager(page, total);
                }
                break;
            case "hupu.ui.bbsreply":
                boolean open = data.getBoolean("open");
                JSONObject extra = data.getJSONObject("extra");
                String tid = extra.getString("tid");
                long pid = extra.getLong("pid");
                String userName = extra.getString("username");
                String content = extra.getString("content");
                if (open) {
                    PostActivity.startActivity(getContext(), Constants.TYPE_REPLY, "", tid,
                            String.valueOf(pid), content);
                }
                break;
            case "hupu.album.view":
                int index = data.getInt("index");
                JSONArray images = data.getJSONArray("images");
                ArrayList<String> extraPics = new ArrayList<>();
                for (int i = 0; i < images.length(); i++) {
                    JSONObject image = images.getJSONObject(i);
                    extraPics.add(image.getString("url"));
                }
                ImagePreviewActivity.startActivity(getContext(), extraPics.get(index), extraPics);
                break;
            case "hupu.ui.copy":
                String copy = data.getString("content");
                StringUtil.copy(getContext(), copy);
                break;
            case "hupu.ui.report":
                JSONObject reportExtra = data.getJSONObject("extra");
                String reportTid = reportExtra.getString("tid");
                long reportPid = reportExtra.getLong("pid");
                ReportActivity.startActivity(getContext(), reportTid, String.valueOf(reportPid));
                break;
            case "hupu.user.login":
                LoginActivity.startActivity(getContext());
                ToastUtil.showToast("请先登录");
                break;
            case "hupu.ui.pageclose":
                AppManager.getAppManager().finishActivity();
                break;
        }
    }

    private void setUA(int i) {
        if (this.basicUA != null) {
            getSettings().setUserAgentString(this.basicUA + " isp/" + i + " network/" + i);
        }
    }

    public void loadUrl(String url) {
        setUA(-1);
        if (header == null) {
            header = new HashMap<>();
            header.put("Accept-Encoding", "gzip");
        }
        super.loadUrl(url, header);
    }

    private HuPuWebViewCallBack callBack;

    public interface HuPuWebViewCallBack {

        void onFinish();

        void onUpdatePager(int page, int total);

        void onError();
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

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }
}
