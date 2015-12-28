package com.gzsll.hupu.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.ThreadListPresenter;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.ThreadActivity;
import com.gzsll.hupu.ui.adapter.ThreadListAdapter;
import com.gzsll.hupu.view.ThreadListView;
import com.gzsll.hupu.widget.NestedScrollRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/3/7.
 */
@EFragment
public class ThreadListFragment extends BaseFragment implements ThreadListView, NestedScrollRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {
    Logger logger = Logger.getLogger(ThreadListFragment.class.getSimpleName());

    @FragmentArg
    String fid;


    @Inject
    ThreadListPresenter mThreadListPresenter;
    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;

    @ViewById
    SimpleDraweeView backdrop;
    @ViewById
    Toolbar toolbar;
    @ViewById
    CollapsingToolbarLayout collapsingToolbar;
    @ViewById
    AppBarLayout appbar;
    @ViewById
    ListView listView;
    @ViewById
    CoordinatorLayout mainContent;
    @ViewById
    NestedScrollRefreshLayout refreshLayout;
    @ViewById
    FloatingActionMenu floatingMenu;
    @ViewById
    FloatingActionButton floatingPost, floatingSwitch, floatingRefresh, floatingAttention;
    @ViewById
    FrameLayout frameLayout;

    ThreadActivity activity;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_thread, null);
    }

    @AfterViews
    void init() {
        activity = (ThreadActivity) getActivity();
        activity.initToolBar(toolbar);
        initPresenter();
        initFloatingButton();
        attachPostButtonToRecycle();
        mAdView = new AdView(activity);
        mAdView.setAdUnitId("ca-app-pub-8075807288160204/8098671171");
        mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        listView.addFooterView(mAdView);
        listView.addHeaderView(mAdView);
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdapter.setActivity(activity);
        listView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(this);
        appbar.addOnOffsetChangedListener(this);
        mThreadListPresenter.onThreadReceive(fid, mSettingPrefHelper.getThreadSort(), null);
    }


    private void initPresenter() {
        mThreadListPresenter.setView(this);
        mThreadListPresenter.initialize();
    }

    private void initFloatingButton() {
        mResourceHelper.setFabBtnColor(activity, floatingPost);
        mResourceHelper.setFabBtnColor(activity, floatingSwitch);
        mResourceHelper.setFabBtnColor(activity, floatingRefresh);
        mResourceHelper.setFabBtnColor(activity, floatingAttention);
        mResourceHelper.setFabMenuColor(activity, floatingMenu);
        if (mSettingPrefHelper.getThreadSort().equals(Constants.THREAD_TYPE_HOT)) {
            floatingSwitch.setLabelText("按发帖时间排序");
        } else {
            floatingSwitch.setLabelText("按回帖时间排序");
        }
    }

    private int mPreviousVisibleItem;

    private void attachPostButtonToRecycle() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    floatingMenu.hideMenuButton(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    floatingMenu.showMenuButton(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });
    }

    @UiThread
    @Override
    public void showLoading() {
        showProgress(true);
    }

    @UiThread
    @Override
    public void hideLoading() {
        showContent(true);
    }

    @UiThread
    @Override
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void renderThreadInfo(Board board) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(board.getBackImg()))
                .setResizeOptions(
                        new ResizeOptions(500, 500))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(backdrop.getController())
                .setAutoPlayAnimations(true)
                .build();
        backdrop.setController(draweeController);
        collapsingToolbar.setTitle(board.getName());
    }


    @UiThread
    @Override
    public void renderThreads(List<Thread> threads) {
        refreshLayout.setRefreshing(false);
        mAdapter.updateItems(threads);
    }

    @Override
    public void attendStatus(int status) {
        if (status == 0) {
            floatingAttention.setImageResource(R.drawable.ic_menu_add);
            floatingAttention.setLabelText("添加关注");
        } else {
            floatingAttention.setImageResource(R.drawable.ic_minus);
            floatingAttention.setLabelText("取消关注");
        }
    }


    @UiThread
    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    @UiThread
    public void onEmpty() {
        showEmpty(true);
    }

    @UiThread
    @Override
    public void onScrollToTop() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onFloatingVisibility(int visibility) {
        floatingMenu.setVisibility(visibility);
    }

    @UiThread
    @Override
    public void onRefreshing(boolean refresh) {
        refreshLayout.setRefreshing(refresh);
    }


    @Click
    void floatingPost() {
        PostActivity_.intent(getActivity()).type(Constants.TYPE_POST).fid(fid).start();
        floatingMenu.toggle(true);
    }

    @Click
    void floatingRefresh() {
        mThreadListPresenter.onRefresh();
        floatingMenu.toggle(true);
    }


    @Click
    void floatingSwitch() {
        if (floatingSwitch.getLabelText().equals("按回帖时间排序")) {
            mThreadListPresenter.onThreadReceive(fid, Constants.THREAD_TYPE_HOT, null);
            floatingSwitch.setLabelText("按发帖时间排序");
        } else {
            mThreadListPresenter.onThreadReceive(fid, Constants.THREAD_TYPE_NEW, null);
            floatingSwitch.setLabelText("按回帖时间排序");
        }
        floatingMenu.toggleMenuButton(true);
    }

    @Click
    void floatingAttention() {
        floatingMenu.toggle(true);
        if (floatingAttention.getLabelText().equals("取消关注")) {
            mThreadListPresenter.delAttention();
        } else {
            mThreadListPresenter.addAttention();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0 || i == -appBarLayout.getTotalScrollRange()) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            mThreadListPresenter.onRefresh();
        } else {
            mThreadListPresenter.onLoadMore();
        }
    }

    @Override
    public void onReloadClicked() {
        mThreadListPresenter.onReload();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_thread, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                logger.debug("onQueryTextSubmit:" + query);
                mThreadListPresenter.onStartSearch(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mThreadListPresenter.onThreadReceive(fid, mSettingPrefHelper.getThreadSort(), null);
                return true;
            }
        });

    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
