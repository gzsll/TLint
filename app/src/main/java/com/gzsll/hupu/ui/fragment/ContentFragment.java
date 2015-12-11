package com.gzsll.hupu.ui.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.ReplyJumpClickEvent;
import com.gzsll.hupu.presenter.ContentPresenter;
import com.gzsll.hupu.support.utils.ConfigHelper;
import com.gzsll.hupu.support.utils.HtmlHelper;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.ui.activity.BrowserActivity_;
import com.gzsll.hupu.ui.activity.ContentActivity;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.ui.adapter.ThreadReplyAdapter;
import com.gzsll.hupu.view.ContentView;
import com.gzsll.hupu.widget.HuPuWebView;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/14.
 */
@EFragment
public class ContentFragment extends BaseFragment implements ContentView, SwipyRefreshLayout.OnRefreshListener, HuPuWebView.HuPuWebViewCallBack, PagePicker.OnJumpListener {

    private Logger logger = Logger.getLogger(ContentFragment.class.getSimpleName());

    @FragmentArg
    String fid;
    @FragmentArg
    String tid;
    @FragmentArg
    int mPage;


    @ViewById
    HuPuWebView webView;
    @ViewById
    SwipyRefreshLayout refreshLayout;
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
        initPicker();
        initFloatingButton();
        webView.setCallBack(this);
        refreshLayout.setOnRefreshListener(this);
        mContentPresenter.onThreadInfoReceive(tid, fid, 1);
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
    public void renderContent(String url, int page, int totalPage) {
        mPagePicker.setMin(1);
        mPagePicker.setMax(totalPage);
        webView.loadUrl(url);
    }

    @Override
    public void reply(String title) {
        PostActivity_.intent(this).type(Constants.TYPE_COMMENT).fid(fid).tid(tid).title(title).start();
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
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
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
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            mContentPresenter.onRefresh();
        } else {
            mContentPresenter.onPageNext();
        }
    }

    @Override
    public void onReloadClicked() {
        mContentPresenter.onReload();
    }

    @Override
    public void onReady() {
        hideLoading();
    }

    @Override
    public void onUpdatePager(int page, int total) {

    }

    @Override
    public void onReply(boolean open, String tid, long pid, String userName, String content) {
        if (open) {
            PostActivity_.intent(this).type(Constants.TYPE_REPLY).fid(fid).tid(tid).title(content).pid(String.valueOf(pid)).start();
        }
    }

    @Override
    public void onViewImage(String extraPic, ArrayList<String> extraPics) {
        ImagePreviewActivity_.intent(this).extraPic(extraPic).extraPics(extraPics).start();
    }

    @Override
    public void onCopy(String copyText) {
        mContentPresenter.copy(copyText);
    }

    @Override
    public void onReport(String tid, long pid) {

    }

    @Override
    public void onOpenBrowser(String url) {
        BrowserActivity_.intent(this).url(url).start();
    }

    @Override
    public void onOpenContent(String tid) {
        ContentActivity_.intent(this).fid(fid).tid(tid).mPage(1).start();
    }

    @Override
    public void onOpenBoard(String boardId) {

    }

}
