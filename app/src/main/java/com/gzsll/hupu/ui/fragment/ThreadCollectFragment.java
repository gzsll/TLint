package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.presenter.ThreadCollectPresenter;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.adapter.ThreadListAdapter;
import com.gzsll.hupu.ui.view.ThreadCollectView;
import com.gzsll.hupu.widget.AutoLoadScrollListener;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class ThreadCollectFragment extends BaseFragment implements ThreadCollectView, PullToRefreshView.OnRefreshListener {

    public static ThreadCollectFragment newInstance() {
        return new ThreadCollectFragment();
    }

    @Inject
    ThreadCollectPresenter mPresenter;
    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    Activity mActivity;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    PullToRefreshView refreshLayout;

    private AutoLoadScrollListener mAutoLoadListener;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.base_phonix_list_layout;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAutoLoadListener = new AutoLoadScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        };
        recyclerView.addOnScrollListener(mAutoLoadListener);
    }

    @Override
    public void initData() {
        mPresenter.onCollectThreadsReceive();
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
    public void renderThreads(List<Thread> threads) {
        refreshLayout.setRefreshing(false);
        mAutoLoadListener.setLoading(false);
        mAdapter.bind(threads);
    }

    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText("没有收藏的帖子");
        showEmpty(true);
    }

    @Override
    public void onScrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefreshing(boolean refresh) {
        refreshLayout.setRefreshing(refresh);
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onReloadClicked() {
        mPresenter.onReload();
    }
}
