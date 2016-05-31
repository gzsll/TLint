package com.gzsll.hupu.ui.content;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.alibaba.fastjson.JSON;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.components.jockeyjs.JockeyHandler;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.widget.H5Callback;
import com.gzsll.hupu.widget.JockeyJsWebView;
import java.util.Map;
import javax.inject.Inject;
import org.apache.log4j.Logger;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentFragment extends BaseFragment
    implements ContentPagerContract.View, H5Callback, JockeyJsWebView.OnScrollChangedCallback {

  private Logger logger = Logger.getLogger(ContentFragment.class.getSimpleName());

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

  @Bind(R.id.webView) JockeyJsWebView webView;

  @Inject ContentPagerPresenter mContentPresenter;

  private String tid;
  private String fid;
  private String pid;
  private int page;

  @Override public void initInjector() {
    getComponent(ContentComponent.class).inject(this);
  }

  @Override public int initContentView() {
    return R.layout.fragment_content;
  }

  @Override public void getBundle(Bundle bundle) {
    tid = bundle.getString("tid");
    fid = bundle.getString("fid");
    pid = bundle.getString("pid");
    page = bundle.getInt("page");
    logger.debug("page:" + page);
  }

  @Override public void initUI(View view) {
    ButterKnife.bind(this, view);
    mContentPresenter.attachView(this);
    webView.setCallback(this);
    webView.initJockey();
    webView.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        logger.debug("consoleMessage:" + consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
      }
    });
  }

  @Override public void initData() {
    webView.loadUrl("file:///android_asset/hupu_thread.html");
  }

  @Override public void onReloadClicked() {

  }

  @Override public void onScroll(int dx, int dy) {
    if (Math.abs(dy) > 4) {
      ContentActivity activity = ((ContentActivity) getActivity());
      if (activity != null) {
        activity.setFloatingMenuVisibility(dy < 0);
      }
    }
  }

  @Override public void onDestroyView() {
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

  @Override public void showLoading() {
    showProgress(true);
  }

  @Override public void hideLoading() {
    showContent(true);
  }

  @Override public void onError() {
    setEmptyText("数据加载失败");
    showError(true);
  }

  @Override public void sendMessageToJS(String handlerName, Object object) {
    webView.sendMessageToJS(handlerName, object);
  }

  @Override public void showReplyUi(String fid, String tid, String pid, String title) {
    PostActivity.startActivity(getActivity(), Constants.TYPE_REPLY, fid, tid, pid, title);
  }

  @Override public void showReportUi(String tid, String pid) {
    ReportActivity.startActivity(getActivity(), tid, pid);
  }

  @Override public void doPerform(Map<Object, Object> map) {

  }

  @Override public void onPageFinished(WebView webView, String str) {
    mContentPresenter.onThreadInfoReceive(tid, fid, pid, page);
  }

  @Override public void onPageStarted(WebView webView, String str, Bitmap bitmap) {

  }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

  }

  @Override public void setJockeyEvents() {
    webView.onJSEvent("showImg", new JockeyHandler() {
      @Override protected void doPerform(Map<Object, Object> payload) {
        logger.debug("showImg:" + JSON.toJSON(payload));
      }
    });
    webView.onJSEvent("showUrl", new JockeyHandler() {
      @Override protected void doPerform(Map<Object, Object> payload) {
        logger.debug("showUrl:" + JSON.toJSON(payload));
      }
    });
    webView.onJSEvent("showUser", new JockeyHandler() {
      @Override protected void doPerform(Map<Object, Object> payload) {
        logger.debug("showUser:" + JSON.toJSON(payload));
      }
    });
    webView.onJSEvent("showMenu", new JockeyHandler() {
      @Override protected void doPerform(Map<Object, Object> payload) {
        logger.debug("showMenu:" + JSON.toJSON(payload));
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

  @Override public void openBrowser(String url) {
    logger.debug("openBrowser:" + url);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mContentPresenter.detachView();
  }
}
