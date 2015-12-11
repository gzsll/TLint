package com.gzsll.hupu.ui.fragment;

import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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
import com.gzsll.hupu.presenter.ThreadListPresenter;
import com.gzsll.hupu.support.storage.bean.Board;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.ThreadActivity;
import com.gzsll.hupu.ui.adapter.ThreadListAdapter;
import com.gzsll.hupu.view.ThreadListView;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/3/7.
 */
@EFragment
public class ThreadListFragment extends BaseFragment implements ThreadListView, SwipyRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {
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
    RecyclerView recyclerView;
    @ViewById
    CoordinatorLayout mainContent;
    @ViewById
    SwipyRefreshLayout refreshLayout;
    @ViewById
    FloatingActionMenu floatingMenu;
    @ViewById
    FloatingActionButton floatingPost, floatingSwitch, floatingRefresh, floatingAttention;

    ThreadActivity activity;

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_thread, null);
    }

    @AfterViews
    void init() {
        activity = (ThreadActivity) getActivity();
        initPresenter();
        initFloatingButton();
        attachPostButtonToRecycle();
        activity.initToolBar(toolbar);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setActivity(activity);
        recyclerView.setAdapter(mAdapter);
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


    private void attachPostButtonToRecycle() {
        //  postButton.attachToRecyclerView(forumsListView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
    public void showLoading() {
        showProgress(true);
    }

    @Override
    public void hideLoading() {
        showContent(true);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void renderThreadInfo(Board board) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(board.backImg))
                .setResizeOptions(
                        new ResizeOptions(500, 500))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(backdrop.getController())
                .setAutoPlayAnimations(true)
                .build();
        backdrop.setController(draweeController);
        collapsingToolbar.setTitle(board.name);
    }

    @Override
    public void renderThreads(List<Thread> threads) {
        refreshLayout.setRefreshing(false);
        mAdapter.updateItems(threads);
    }

    @Override
    public void attendStatus(int status) {

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
    public void onScrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        mThreadListPresenter.addAttention();
        floatingMenu.toggle(true);
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
}
