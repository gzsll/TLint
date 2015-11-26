package com.gzsll.hupu.ui.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.ReplyJumpClickEvent;
import com.gzsll.hupu.presenter.ContentPresenter;
import com.gzsll.hupu.support.storage.bean.ThreadImage;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItems;
import com.gzsll.hupu.support.utils.ConfigHelper;
import com.gzsll.hupu.support.utils.HtmlHelper;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.ui.activity.BrowserActivity_;
import com.gzsll.hupu.ui.activity.ContentActivity;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.ui.adapter.ThreadReplyAdapter;
import com.gzsll.hupu.view.ContentView;
import com.gzsll.hupu.widget.H5Callback;
import com.gzsll.hupu.widget.JockeyJsWebView;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.PinnedHeaderListView;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;
import com.jockeyjs.JockeyAsyncHandler;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/14.
 */
@EFragment
public class ContentFragment extends BaseFragment implements ContentView, SwipyRefreshLayout.OnRefreshListener, H5Callback, PagePicker.OnJumpListener {

    private Logger logger = Logger.getLogger(ContentFragment.class.getSimpleName());

    @FragmentArg
    long mThreadId;
    @FragmentArg
    int mPage;


    @ViewById
    SwipyRefreshLayout refreshLayout;
    @ViewById
    PinnedHeaderListView recyclerView;
    @ViewById
    FloatingActionMenu floatingMenu;
    @ViewById
    FloatingActionButton floatingComment, floatingFav, floatingShare;


    @Inject
    ContentPresenter mContentPresenter;
    @Inject
    Gson mGson;
    @Inject
    ThreadReplyAdapter mAdapter;
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    Bus mBus;
    @Inject
    HtmlHelper mHtmlHelper;
    @Inject
    ConfigHelper mConfigHelper;


    private JockeyJsWebView mWebView;
    private PagePicker mPagePicker;
    private ContentActivity mActivity;

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_content, null);
    }


    @AfterViews
    void init() {
        mBus.register(this);
        mActivity = (ContentActivity) getActivity();
        mContentPresenter.setView(this);
        mContentPresenter.initialize();
        initWebView();
        initRecyclerView();
        initPicker();
        initFloatingButton();
    }


    private void initWebView() {
        mWebView = new JockeyJsWebView(mActivity);
        mWebView.setCallback(this);
        mWebView.initJockey();
        loadWebContent();
    }

    @Background
    void loadWebContent() {
        String html = mHtmlHelper.getHtmlString();
        loadWebFinish(html);
    }

    @UiThread
    void loadWebFinish(String html) {
        mWebView.loadDataWithBaseURL(String.format("file://%s", mConfigHelper.getCachePath()), html, "text/html",
                "utf-8", null);
    }

    private String stringFromAssetsFile(String fileName) {
        AssetManager manager = getActivity().getAssets();
        InputStream file;
        try {
            file = manager.open(fileName);
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initRecyclerView() {
        recyclerView.addHeaderView(mWebView);
        mAdapter.setActivity(mActivity);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initPicker() {
        mPagePicker = new PagePicker(mActivity);
        mPagePicker.setOnJumpListener(this);
    }

    private void initFloatingButton() {
        mResourceHelper.setFabMenuColor(mActivity, floatingMenu);
        mResourceHelper.setFabBtnColor(mActivity, floatingComment);
        mResourceHelper.setFabBtnColor(mActivity, floatingFav);
        mResourceHelper.setFabBtnColor(mActivity, floatingShare);
    }

    @Override
    @UiThread(delay = 100)
    public void renderContent(Map<Object, Object> map) {
        mWebView.sendMessageToJS("initThreadInfo", map, null);
    }

    @Override
    public void renderReplies(int page, int totalPage, List<ThreadReplyItems> replyItems) {
        refreshLayout.setRefreshing(false);
        mPagePicker.setMin(1);
        mPagePicker.setMax(totalPage);
        mAdapter.bindData(replyItems);
        if (page > 1) {
            mWebView.setWebViewHeight(1);
            recyclerView.setSelection(0);

        }
    }

    @Override
    public void reply(String title) {
        PostActivity_.intent(this).type(Constants.TYPE_COMMENT).groupThreadId(String.valueOf(mThreadId)).title(title).start();
    }

    @Override
    public void pm(String author) {

    }

    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    public void onEmpty() {
        showEmpty(true);
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
    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doPerform(Map<Object, Object> map) {

    }

    @Override
    public boolean onJsTimeout() {
        return false;
    }

    @Override
    public void onPageFinished(WebView webView, String str) {
        logger.debug("onPageFinished");
        mContentPresenter.onThreadInfoReceive(mThreadId, 1);
    }

    @Override
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        logger.debug("onPageStarted:" + str);
    }

    @Override
    public void onReceivedError(WebView webView, int i, String str, String str2) {

    }

    @Override
    public void setJockeyEvents() {
        mWebView.onJSEvent("h", new JockeyAsyncHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                logger.debug("h:" + mGson.toJson(payload));
                double height = (double) payload.get("h");
                logger.debug("height:" + height);
                setWebViewHeight((int) height);

            }
        });
        mWebView.onJSEvent("myPage", new JockeyAsyncHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                long uid = Double.valueOf((double) payload.get("uid")).longValue();
                showUser(String.valueOf(uid));
            }
        });

        mWebView.onJSEvent("showImg", new JockeyAsyncHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                ThreadImage threadImage = mGson.fromJson(mGson.toJson(payload), ThreadImage.class);
                showImg(threadImage);
            }
        });
    }

    @Override
    public void openBrowser(String url) {
        BrowserActivity_.intent(this).url(url).start();
    }


    @UiThread
    void setWebViewHeight(int height) {
        mWebView.setWebViewHeight(height);
    }


    @UiThread
    void showImg(ThreadImage threadImage) {
        ImagePreviewActivity_.intent(this).extraPic(threadImage.getImgs().get((int) threadImage.getIndex())).extraPics(threadImage.getImgs()).start();
    }

    @UiThread
    void showUser(String uid) {
        UserProfileActivity_.intent(this).uid(uid).start();
    }

    @Override
    public void OnJump(int page) {
        mContentPresenter.onPageSelected(page);
    }


    @ViewById
    FrameLayout frameLayout;

    @Subscribe
    @UiThread
    public void onReplyJumpClickEvent(ReplyJumpClickEvent event) {
        logger.debug("onReplyJumpClickEvent");
        mPagePicker.setValue(event.getCurrentPage());
        mPagePicker.showAtLocation(frameLayout, Gravity.BOTTOM, 0, 0);
    }


    @Click
    void floatingComment() {
        mContentPresenter.reply();
        floatingMenu.toggle(true);
    }

    @Click
    void floatingFav() {
        mContentPresenter.addFavorite();
        floatingMenu.toggle(true);
    }

    @Click
    void floatingShare() {
        floatingMenu.toggle(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            mContentPresenter.onRefresh();
        } else {
            mContentPresenter.onPageNext();
        }
    }

    @Override
    public void onReloadClicked() {
        mContentPresenter.onReload();
    }
}
