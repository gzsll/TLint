package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.presenter.ContentPresenter;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.activity.PostActivity;
import com.gzsll.hupu.ui.activity.ReportActivity;
import com.gzsll.hupu.ui.view.ContentView;
import com.gzsll.hupu.widget.HuPuWebView;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentFragment extends BaseFragment implements ContentView, SwipyRefreshLayout.OnRefreshListener, HuPuWebView.HuPuWebViewCallBack, HuPuWebView.OnScrollChangedCallback, PagePicker.OnJumpListener {


    public static ContentFragment newInstance(String fid, String tid, String pid, int page, String title) {
        ContentFragment mFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("tid", tid);
        bundle.putString("pid", pid);
        bundle.putInt("page", page);
        bundle.putString("title", title);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Bind(R.id.webView)
    HuPuWebView webView;
    @Bind(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @Bind(R.id.floatingComment)
    FloatingActionButton floatingComment;
    @Bind(R.id.floatingReport)
    FloatingActionButton floatingReport;
    @Bind(R.id.floatingCollect)
    FloatingActionButton floatingCollect;
    @Bind(R.id.floatingShare)
    FloatingActionButton floatingShare;
    @Bind(R.id.floatingMenu)
    FloatingActionMenu floatingMenu;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.deliver)
    View deliver;
    @Bind(R.id.tvPre)
    TextView tvPre;
    @Bind(R.id.tvPageNum)
    TextView tvPageNum;
    @Bind(R.id.tvNext)
    TextView tvNext;


    @Inject
    ContentPresenter mPresenter;
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    Activity mActivity;
    @Inject
    RequestHelper mRequestHelper;

    private PagePicker mPagePicker;
    private String fid;
    private String tid;
    private int page;
    private String pid;
    private String title;

    private boolean isCollect;
    private String shareText;
    private String shareUrl;


    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_content;
    }

    @Override
    public void getBundle(Bundle bundle) {
        fid = bundle.getString("fid");
        tid = bundle.getString("tid");
        page = bundle.getInt("page");
        pid = bundle.getString("pid");
        title = bundle.getString("title");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        initPicker();
        initFloatingButton();
        webView.setCallBack(this);
        webView.setOnScrollChangedCallback(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initPicker() {
        mPagePicker = new PagePicker(mActivity);
        mPagePicker.setOnJumpListener(this);
    }

    private void initFloatingButton() {
        mResourceHelper.setFabMenuColor(mActivity, floatingMenu);
        mResourceHelper.setFabBtnColor(mActivity, floatingComment);
        mResourceHelper.setFabBtnColor(mActivity, floatingCollect);
        mResourceHelper.setFabBtnColor(mActivity, floatingShare);
        mResourceHelper.setFabBtnColor(mActivity, floatingReport);
    }

    @Override
    public void initData() {
        mPresenter.onThreadInfoReceive(tid, fid, pid, page);
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
    public void renderContent(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void renderShare(String share, String url) {
        shareText = share;
        shareUrl = url;
    }

    @Override
    public void isCollected(boolean isCollected) {
        isCollect = isCollected;
        floatingCollect.setImageResource(isCollected ? R.drawable.ic_menu_star : R.drawable.ic_menu_star_outline);
        floatingCollect.setLabelText(isCollected ? "取消收藏" : "收藏");
    }


    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }


    @Override
    public void onReady() {
        hideLoading();
    }

    @Override
    public void onUpdatePager(int page, int totalPage) {
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
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            mPresenter.onRefresh();
        } else {
            mPresenter.onPageNext();
        }
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

    @Override
    public void OnJump(int page) {
        mPresenter.onPageSelected(page);
    }

    @OnClick(R.id.floatingComment)
    void setFloatingCommentClick() {
        PostActivity.startActivity(mActivity, Constants.TYPE_COMMENT, fid, tid, "", title);
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingShare)
    void floatingShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingReport)
    void floatingReport() {
        ReportActivity.startActivity(mActivity, tid, "");
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingCollect)
    void floatingCollect() {
        if (isCollect) {
            mPresenter.delCollect();
        } else {
            mPresenter.addCollect();
        }
        floatingMenu.toggle(true);
    }


    @OnClick(R.id.tvPre)
    void tvPre() {
        mPresenter.onPagePre();
    }


    @OnClick(R.id.tvNext)
    void tvNext() {
        mPresenter.onPageNext();
    }

    @OnClick(R.id.tvPageNum)
    void tvPageNum() {
        mPagePicker.showAtLocation(frameLayout, Gravity.BOTTOM, 0, 0);
    }
}
