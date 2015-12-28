package com.gzsll.hupu.ui.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.ContentPresenter;
import com.gzsll.hupu.support.utils.ConfigHelper;
import com.gzsll.hupu.support.utils.HtmlHelper;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.ui.activity.BrowserActivity_;
import com.gzsll.hupu.ui.activity.ContentActivity;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.ReportActivity_;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.ui.adapter.ThreadReplyAdapter;
import com.gzsll.hupu.view.ContentView;
import com.gzsll.hupu.widget.HuPuWebView;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;
import com.squareup.otto.Bus;

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
public class ContentFragment extends BaseFragment implements ContentView, SwipyRefreshLayout.OnRefreshListener, HuPuWebView.HuPuWebViewCallBack, HuPuWebView.OnScrollChangedCallback, PagePicker.OnJumpListener {

    private Logger logger = Logger.getLogger(ContentFragment.class.getSimpleName());

    @FragmentArg
    String fid;
    @FragmentArg
    String tid;
    @FragmentArg
    int page;
    @FragmentArg
    String pid;
    @FragmentArg
    String title;


    @ViewById
    HuPuWebView webView;
    @ViewById
    SwipyRefreshLayout refreshLayout;
    @ViewById
    FloatingActionMenu floatingMenu;
    @ViewById
    FloatingActionButton floatingComment, floatingFav, floatingShare, floatingReport;
    @ViewById
    TextView tvPre, tvPageNum, tvNext;


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
        webView.setOnScrollChangedCallback(this);
        refreshLayout.setOnRefreshListener(this);
        mContentPresenter.onThreadInfoReceive(tid, fid, pid, 1);
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
        mResourceHelper.setFabBtnColor(mActivity, floatingReport);
    }


    @Override
    public void renderContent(String url, int page, int totalPage) {
        mPagePicker.setMin(1);
        mPagePicker.setMax(totalPage);
        mPagePicker.setValue(page);
        tvPageNum.setText(page + "/" + totalPage);
        if (page == 1) {
            tvPre.setTextColor(getResources().getColor(R.color.base_text_gray));
            tvPre.setClickable(false);
        } else {
            tvPre.setTextColor(getResources().getColor(R.color.blue));
            tvPre.setClickable(true);
        }

        if (page == totalPage) {
            tvNext.setTextColor(getResources().getColor(R.color.base_text_gray));
            tvNext.setClickable(false);
        } else {
            tvNext.setTextColor(getResources().getColor(R.color.blue));
            tvNext.setClickable(true);
        }
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



    @Click
    void floatingComment() {
        mContentPresenter.reply(title);
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

    @Click
    void floatingReport() {
        ReportActivity_.intent(this).tid(tid).start();
    }


    @Click
    void tvPre() {
        mContentPresenter.onPagePre();
    }


    @Click
    void tvNext() {
        mContentPresenter.onPageNext();
    }

    @Click
    void tvPageNum() {
        mPagePicker.showAtLocation(frameLayout, Gravity.BOTTOM, 0, 0);
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
        ReportActivity_.intent(this).tid(tid).pid(String.valueOf(pid)).start();
    }

    @Override
    public void onOpenBrowser(String url) {
        BrowserActivity_.intent(this).url(url).start();
    }

    @Override
    public void onOpenContent(String tid) {
        ContentActivity_.intent(this).fid(fid).tid(tid).page(1).start();
    }

    @Override
    public void onOpenBoard(String boardId) {

    }

    @Override
    public void onScroll(int dx, int dy) {
        if (Math.abs(dy) > 4) {
            if (dy > 0) {
                floatingMenu.hideMenuButton(true);
            } else {
                floatingMenu.showMenuButton(true);
            }
        }
    }
}
