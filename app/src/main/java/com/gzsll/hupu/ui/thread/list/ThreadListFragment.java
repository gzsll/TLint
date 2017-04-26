package com.gzsll.hupu.ui.thread.list;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.thread.ThreadListAdapter;
import com.gzsll.hupu.util.ResourceUtil;
import com.gzsll.hupu.util.SettingPrefUtil;
import com.gzsll.hupu.widget.LoadMoreRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 */
public class ThreadListFragment extends BaseFragment
        implements ThreadListContract.View, SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener, LoadMoreRecyclerView.LoadMoreListener {


    @Inject
    ThreadListPresenter mPresenter;
    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    Activity mActivity;

    @BindView(R.id.backdrop)
    SimpleDraweeView backdrop;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.floatingAttention)
    FloatingActionButton floatingAttention;
    @BindView(R.id.floatingPost)
    FloatingActionButton floatingPost;
    @BindView(R.id.floatingSwitch)
    FloatingActionButton floatingSwitch;
    @BindView(R.id.floatingRefresh)
    FloatingActionButton floatingRefresh;
    @BindView(R.id.floatingMenu)
    FloatingActionMenu floatingMenu;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initInjector() {
        getComponent(ThreadListComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_thread;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        ((ThreadListActivity) mActivity).initToolBar(toolbar);
        initFloatingButton();
        attachPostButtonToRecycle();
        refreshLayout.setOnRefreshListener(this);
        appbar.addOnOffsetChangedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLoadMoreListener(this);
    }

    private void initFloatingButton() {
        ResourceUtil.setFabBtnColor(mActivity, floatingPost);
        ResourceUtil.setFabBtnColor(mActivity, floatingSwitch);
        ResourceUtil.setFabBtnColor(mActivity, floatingRefresh);
        ResourceUtil.setFabBtnColor(mActivity, floatingAttention);
        ResourceUtil.setFabMenuColor(mActivity, floatingMenu);
        if (SettingPrefUtil.getThreadSort(mActivity).equals(Constants.THREAD_TYPE_HOT)) {
            floatingSwitch.setLabelText("按发帖时间排序");
        } else {
            floatingSwitch.setLabelText("按回帖时间排序");
        }
    }

    private void attachPostButtonToRecycle() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (Math.abs(dy) > 4) {
                    if (dy > 0) {
                        floatingMenu.hideMenuButton(true);
                    } else {
                        floatingMenu.showMenuButton(true);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.onThreadReceive(SettingPrefUtil.getThreadSort(mActivity));
    }

    @Override
    public void showLoading() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }, 5);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showContent() {
        showContent(true);
    }

    @Override
    public void renderThreadInfo(Forum forum) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(forum.getBackImg()))
                .setResizeOptions(new ResizeOptions(500, 500))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(backdrop.getController())
                .setAutoPlayAnimations(true)
                .build();
        backdrop.setController(draweeController);
        collapsingToolbar.setTitle(forum.getName());
        tvSubTitle.setText(forum.getDescription());
    }

    @Override
    public void renderThreads(List<Thread> threads) {
        mAdapter.bind(threads);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoadCompleted(boolean hasMore) {
        recyclerView.onLoadCompleted(hasMore);
    }

    @Override
    public void onRefreshCompleted() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void attendStatus(boolean isAttention) {
        if (!isAttention) {
            floatingAttention.setImageResource(R.drawable.ic_menu_add);
            floatingAttention.setLabelText("添加关注");
        } else {
            floatingAttention.setImageResource(R.drawable.ic_minus);
            floatingAttention.setLabelText("取消关注");
        }
    }

    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText("暂无帖子");
        showEmpty(true);
    }

    @Override
    public void onScrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onFloatingVisibility(int visibility) {
        floatingMenu.setVisibility(visibility);
    }

    @Override
    public void showPostThreadUi(String fid) {
        PostActivity.startActivity(mActivity, Constants.TYPE_POST, fid, "", "", "");
    }

    @Override
    public void showLoginUi() {
        LoginActivity.startActivity(mActivity);
    }

    @Override
    public void showToast(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0 || verticalOffset == -appBarLayout.getTotalScrollRange()) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void onReloadClicked() {
        mPresenter.onReload();
    }

    @OnClick(R.id.floatingAttention)
    void floatingAttention() {
        mPresenter.onAttentionClick();
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingPost)
    void floatingPost() {
        mPresenter.onPostClick();
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingRefresh)
    void floatingRefresh() {
        mPresenter.onRefresh();
        floatingMenu.toggle(true);
    }

    @OnClick(R.id.floatingSwitch)
    void floatingSwitch() {
        if (floatingSwitch.getLabelText().equals("按回帖时间排序")) {
            mPresenter.onThreadReceive(Constants.THREAD_TYPE_HOT);
            floatingSwitch.setLabelText("按发帖时间排序");
        } else {
            mPresenter.onThreadReceive(Constants.THREAD_TYPE_NEW);
            floatingSwitch.setLabelText("按回帖时间排序");
        }
        floatingMenu.toggleMenuButton(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_thread, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.onStartSearch(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mPresenter.onThreadReceive(SettingPrefUtil.getThreadSort(mActivity));
                        return true;
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }
}
