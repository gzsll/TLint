package com.gzsll.hupu.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.NetWorkHelper;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/10.
 */
public class HuPuWebView extends WebView {

    private Logger logger = Logger.getLogger(HuPuWebView.class.getSimpleName());

    private String basicUA;

    @Inject
    UserStorage mUserStorage;
    @Inject
    NetWorkHelper mNetWorkHelper;


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


    public class HuPuChromeClient extends WebChromeClient {

    }

    private void init() {
        ((AppApplication) getContext().getApplicationContext()).getObjectGraph().inject(this);
        WebSettings settings = getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(1);
        settings.setLoadsImagesAutomatically(false);
        settings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT > 7) {
            settings.setPluginState(WebSettings.PluginState.ON);
        }
        if (Build.VERSION.SDK_INT > 6) {
            settings.setAppCacheEnabled(true);
            settings.setLoadWithOverviewMode(true);
        }
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        String path = getContext().getFilesDir().getPath();
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(path);
        settings.setDomStorageEnabled(true);
        this.basicUA = settings.getUserAgentString() + " kanqiu/0.7.5/1";

        initWebViewClient();

        try {

            if (mUserStorage.isLogin()) {
                String token = mUserStorage.getToken();
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setCookie("http://www.hupu.com", "u="
                        + URLEncoder.encode(token, "utf-8") + ";"
                        + " path=/; domain=.hupu.com;");
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
            logger.debug(Uri.decode(url));
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            logger.debug("scheme:" + scheme);
            if (scheme != null) {
                handleScheme(scheme, url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }

        }
    }

    private void handleScheme(String scheme, String url) {
        if (scheme != null) {
            if (scheme.equalsIgnoreCase("kanqiu")) {
                handleKanQiu(url);
            } else if (scheme.equalsIgnoreCase("market")) {
            } else if (scheme.equalsIgnoreCase("tel")) {

            } else if (scheme.equalsIgnoreCase("smsto") || scheme.equalsIgnoreCase("sms")) {

            } else if (scheme.equalsIgnoreCase("mailto")) {

            } else if (scheme.equalsIgnoreCase("browser")) {
                callBack.onOpenBrowser(url);
            } else if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                callBack.onOpenBrowser(url);
            } else if (scheme.equalsIgnoreCase("hupu")) {
                try {
                    JSONObject object = new JSONObject(Uri.decode(url.substring("hupu".length() + 3)));
                    String method = object.getString("method");
                    handleHuPu(method, object.getJSONObject("data"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void handleKanQiu(String url) {
        if (url.contains("topic")) {
            String tid = url.substring(url.lastIndexOf("/") + 1);
            logger.debug("tid:" + tid);
            callBack.onOpenContent(tid);
        } else if (url.contains("board")) {
            String boardId = url.substring(url.lastIndexOf("/") + 1);
            logger.debug("boardId:" + boardId);
            callBack.onOpenBoard(boardId);
        }
    }


    private void handleHuPu(String method, JSONObject data) throws Exception {
        switch (method) {
            case "bridgeReady":
                callBack.onReady();
                break;
            case "hupu.ui.updatebbspager":
                int page = data.getInt("page");
                int total = data.getInt("total");
                callBack.onUpdatePager(page, total);
                break;
            case "hupu.ui.bbsreply":
                boolean open = data.getBoolean("open");
                JSONObject extra = data.getJSONObject("extra");
                String tid = extra.getString("tid");
                long pid = extra.getLong("pid");
                String userName = extra.getString("username");
                String content = extra.getString("content");
                callBack.onReply(open, tid, pid, userName, content);
                break;
            case "hupu.album.view":
                int index = data.getInt("index");
                JSONArray images = data.getJSONArray("images");
                ArrayList<String> extraPics = new ArrayList<>();
                for (int i = 0; i < images.length(); i++) {
                    JSONObject image = images.getJSONObject(i);
                    extraPics.add(image.getString("url"));
                }
                callBack.onViewImage(extraPics.get(index), extraPics);
                break;
            case "hupu.ui.copy":
                String copy = data.getString("content");
                callBack.onCopy(copy);
                break;
            case "hupu.ui.report":
                JSONObject reportExtra = data.getJSONObject("extra");
                String reportTid = reportExtra.getString("tid");
                long reportPid = reportExtra.getLong("pid");
                callBack.onReport(reportTid, reportPid);
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
        super.loadUrl(url);
    }


    private HuPuWebViewCallBack callBack;


    public interface HuPuWebViewCallBack {

        void onReady();

        void onUpdatePager(int page, int total);

        void onReply(boolean open, String tid, long pid, String userName, String content);

        void onViewImage(String extraPic, ArrayList<String> extraPics);

        void onCopy(String copyText);

        void onReport(String tid, long pid);

        void onOpenBrowser(String url);

        void onOpenContent(String tid);

        void onOpenBoard(String boardId);


    }


}
