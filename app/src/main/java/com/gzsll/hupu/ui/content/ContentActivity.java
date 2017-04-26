package com.gzsll.hupu.ui.content;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
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
import com.gzsll.hupu.injector.HasComponent;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.util.DisplayUtil;
import com.gzsll.hupu.util.ResourceUtil;
import com.gzsll.hupu.widget.PagePicker;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;
import com.gzsll.hupu.widget.VerticalViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentActivity extends BaseSwipeBackActivity
        implements ContentContract.View, PagePicker.OnJumpListener, ViewPager.OnPageChangeListener,
        HasComponent<ContentComponent> {

    @BindView(R.id.viewPager)
    VerticalViewPager viewPager;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.progress_container)
    LinearLayout progressContainer;
    @BindView(R.id.rlProgress)
    RelativeLayout rlProgress;
    @BindView(R.id.floatingComment)
    FloatingActionButton floatingComment;
    @BindView(R.id.floatingReport)
    FloatingActionButton floatingReport;
    @BindView(R.id.floatingCollect)
    FloatingActionButton floatingCollect;
    @BindView(R.id.floatingShare)
    FloatingActionButton floatingShare;
    @BindView(R.id.floatingMenu)
    FloatingActionMenu floatingMenu;
    @BindView(R.id.tvPre)
    TextView tvPre;
    @BindView(R.id.tvPageNum)
    TextView tvPageNum;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.rlPage)
    RelativeLayout rlPage;
    @BindView(R.id.rlError)
    RelativeLayout rlError;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.progress_view)
    ProgressBarCircularIndeterminate progressBar;

    public static void startActivity(Context mContext, String fid, String tid, String pid, int page) {
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        intent.putExtra("page", page);
        mContext.startActivity(intent);
    }

    @Inject
    ContentPresenter mPresenter;

    private String fid;
    private String tid;
    private int page;
    private String pid;

    private PagePicker mPagePicker;
    private MyAdapter mAdapter;
    private ContentComponent mContentComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_content;
    }

    @Override
    public void initInjector() {
        mContentComponent = DaggerContentComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .contentModule(new ContentModule())
                .build();
        mContentComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        fid = getIntent().getStringExtra("fid");
        tid = getIntent().getStringExtra("tid");
        page = getIntent().getIntExtra("page", 1);
        pid = getIntent().getStringExtra("pid");
        initPicker();
        initFloatingButton();
        viewPager.setOnPageChangeListener(this);
        progressBar.setBackgroundColor(ResourceUtil.getThemeColor(this));
        mPresenter.onThreadInfoReceive(tid, fid, pid, page);
    }

    private void initPicker() {
        mPagePicker = new PagePicker(this);
        mPagePicker.setOnJumpListener(this);
    }

    private void initFloatingButton() {
        ResourceUtil.setFabMenuColor(this, floatingMenu);
        ResourceUtil.setFabBtnColor(this, floatingComment);
        ResourceUtil.setFabBtnColor(this, floatingCollect);
        ResourceUtil.setFabBtnColor(this, floatingShare);
        ResourceUtil.setFabBtnColor(this, floatingReport);
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
    public void renderContent(int page, int totalPage) {
        mAdapter = new MyAdapter(getFragmentManager(), totalPage);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(page - 1);
        onUpdatePager(page, totalPage);
    }

    @Override
    public void setCurrentItem(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPresenter.updatePage(position + 1);
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
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public ContentComponent getComponent() {
        return mContentComponent;
    }

    public class MyAdapter extends FragmentPagerAdapter {
        private int totalPage;

        public MyAdapter(FragmentManager fm, int totalPage) {
            super(fm);
            this.totalPage = totalPage;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(fid, tid, pid, position + 1);
        }

        @Override
        public int getCount() {
            return totalPage;
        }
    }

    @Override
    public void isCollected(boolean isCollected) {
        floatingCollect.setImageResource(
                isCollected ? R.drawable.ic_menu_star : R.drawable.ic_menu_star_outline);
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
    public void onToggleFloatingMenu() {
        floatingMenu.toggle(true);
    }

    @Override
    public void showLoginUi() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void showReportUi() {
        ReportActivity.startActivity(this, tid, "");
    }

    @Override
    public void showPostUi(String title) {
        PostActivity.startActivity(this, Constants.TYPE_COMMENT, fid, tid, "", title);
    }

    @Override
    public void OnJump(int page) {
        mPresenter.onPageSelected(page);
    }

    @OnClick(R.id.floatingComment)
    void setFloatingCommentClick() {
        mPresenter.onCommendClick();
    }

    @OnClick(R.id.floatingShare)
    void floatingShare() {
        mPresenter.onShareClick();
    }

    @OnClick(R.id.floatingReport)
    void floatingReport() {
        mPresenter.onReportClick();
    }

    @OnClick(R.id.floatingCollect)
    void floatingCollect() {
        mPresenter.onCollectClick();
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
            mPagePicker.showAtLocation(frameLayout, Gravity.BOTTOM, 0, DisplayUtil.dip2px(this, 40));
        }
    }

    @OnClick(R.id.btnReload)
    void btnReload() {
        mPresenter.onReload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public void setFloatingMenuVisibility(boolean show) {
        if (show) {
            floatingMenu.showMenu(true);
        } else {
            floatingMenu.hideMenu(true);
        }
    }
}
