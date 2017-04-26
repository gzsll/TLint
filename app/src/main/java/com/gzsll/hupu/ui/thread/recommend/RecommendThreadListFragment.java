package com.gzsll.hupu.ui.thread.recommend;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.main.MainComponent;
import com.gzsll.hupu.ui.thread.ThreadListAdapter;
import com.gzsll.hupu.widget.LoadMoreRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/5/11.
 */
public class RecommendThreadListFragment extends BaseFragment
        implements RecommendThreadListContract.View, PullToRefreshView.OnRefreshListener,
        LoadMoreRecyclerView.LoadMoreListener {

    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    Activity mActivity;
    @Inject
    ThreadRecommendPresenter mRecommendPresenter;

    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PullToRefreshView refreshLayout;

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
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
        mRecommendPresenter.attachView(this);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadMoreListener(this);
    }

    @Override
    public void initData() {
        mRecommendPresenter.onThreadReceive();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        showContent(true);
    }

    @Override
    public void renderThreads(List<Thread> threads) {
        mAdapter.bind(threads);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText("没有推荐帖子");
        showEmpty(true);
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
    public void onRefresh() {
        mRecommendPresenter.onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecommendPresenter.detachView();
    }

    @Override
    public void onReloadClicked() {
        mRecommendPresenter.onReload();
    }

    @Override
    public void onLoadMore() {
        mRecommendPresenter.onLoadMore();
    }
}
