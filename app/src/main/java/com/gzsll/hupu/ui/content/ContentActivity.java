package com.gzsll.hupu.ui.content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.DisplayHelper;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.otto.ContentScrollEvent;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;
import com.gzsll.hupu.widget.VerticalViewPager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentActivity extends BaseSwipeBackActivity implements ContentContract.View, PagePicker.OnJumpListener, ViewPager.OnPageChangeListener {

    private Logger logger = Logger.getLogger(ContentActivity.class.getSimpleName());

    @Bind(R.id.viewPager)
    VerticalViewPager viewPager;
    @Bind(R.id.tvLoading)
    TextView tvLoading;
    @Bind(R.id.progress_container)
    LinearLayout progressContainer;
    @Bind(R.id.rlProgress)
    RelativeLayout rlProgress;
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
    @Bind(R.id.tvPre)
    TextView tvPre;
    @Bind(R.id.tvPageNum)
    TextView tvPageNum;
    @Bind(R.id.tvNext)
    TextView tvNext;
    @Bind(R.id.rlPage)
    RelativeLayout rlPage;
    @Bind(R.id.rlError)
    RelativeLayout rlError;
    @Bind(R.id.tvError)
    TextView tvError;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.progress_view)
    ProgressBarCircularIndeterminate progressBar;

    public static void startActivity(Context mContext, String fid, String tid, String pid, int page, String title) {
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        intent.putExtra("page", page);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }


    @Inject
    ContentPresenter mPresenter;
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    RequestHelper mRequestHelper;
    @Inject
    Bus mBus;
    @Inject
    UserStorage mUserStorage;


    private String fid;
    private String tid;
    private int page;
    private String pid;
    private String title;

    private boolean isCollect;
    private String shareText;
    private String shareUrl;
    private PagePicker mPagePicker;
    private int totalPage;
    private MyAdapter mAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_content;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        mBus.register(this);
        fid = getIntent().getStringExtra("fid");
        tid = getIntent().getStringExtra("tid");
        page = getIntent().getIntExtra("page", 1);
        pid = getIntent().getStringExtra("pid");
        title = getIntent().getStringExtra("title");
        initPicker();
        initFloatingButton();
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(this);
        progressBar.setBackgroundColor(mResourceHelper.getThemeColor(this));
        mPresenter.onThreadInfoReceive(tid, fid, pid, page);
    }


    private void initPicker() {
        mPagePicker = new PagePicker(this);
        mPagePicker.setOnJumpListener(this);
    }

    private void initFloatingButton() {
        mResourceHelper.setFabMenuColor(this, floatingMenu);
        mResourceHelper.setFabBtnColor(this, floatingComment);
        mResourceHelper.setFabBtnColor(this, floatingCollect);
        mResourceHelper.setFabBtnColor(this, floatingShare);
        mResourceHelper.setFabBtnColor(this, floatingReport);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }


    @Override
    public void showLoading() {
        rlProgress.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        rlProgress.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void renderContent(String url, List<String> urls) {
        totalPage = urls.size();
        if (mAdapter == null) {
            mAdapter = new MyAdapter(getSupportFragmentManager(), urls);
            viewPager.setAdapter(mAdapter);
        }
        viewPager.setCurrentItem(urls.indexOf(url));
        onUpdatePager(viewPager.getCurrentItem() + 1, totalPage);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        onUpdatePager(position + 1, totalPage);
        mPresenter.updatePage(position + 1);
    }


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
    public void onPageScrollStateChanged(int state) {

    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private List<String> urls = new ArrayList<>();

        public MyAdapter(FragmentManager fm, List<String> urls) {
            super(fm);
            this.urls = urls;
            notifyDataSetChanged();
        }


        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(urls.get(position));
        }

        @Override
        public int getCount() {
            return urls.size();
        }

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
        tvError.setText(error);
        rlProgress.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);

    }


    @Override
    public void OnJump(int page) {
        mPresenter.onPageSelected(page);
    }


    @OnClick(R.id.floatingComment)
    void setFloatingCommentClick() {
        if (isLogin()) {
            PostActivity.startActivity(this, Constants.TYPE_COMMENT, fid, tid, "", title);
        }
        floatingMenu.toggle(true);
    }


    @OnClick(R.id.floatingShare)
    void floatingShare() {
        mPresenter.onShare(shareText);
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingReport)
    void floatingReport() {
        if (isLogin()) {
            ReportActivity.startActivity(this, tid, "");
        }
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingCollect)
    void floatingCollect() {
        if (isLogin()) {
            if (isCollect) {
                mPresenter.delCollect();
            } else {
                mPresenter.addCollect();
            }
        }
        floatingMenu.toggle(true);
    }

    private boolean isLogin() {
        if (!mUserStorage.isLogin()) {
            LoginActivity.startActivity(this);
            return false;
        }
        return true;
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
        if (mPagePicker.isShowing()) {
            mPagePicker.dismiss();
        } else {
            mPagePicker.showAtLocation(frameLayout, Gravity.BOTTOM, 0, DisplayHelper.dip2px(this, 40));
        }
    }

    @OnClick(R.id.btnReload)
    void btnReload() {
        mPresenter.onReload();
    }


    @Override
    protected void onDestroy() {
        mBus.unregister(this);
        super.onDestroy();
        mPresenter.detachView();
    }

    @Subscribe
    public void onContentScrollEvent(ContentScrollEvent event) {
        if (event.isShow()) {
            floatingMenu.showMenu(true);
        } else {
            floatingMenu.hideMenu(true);
        }
    }
}
