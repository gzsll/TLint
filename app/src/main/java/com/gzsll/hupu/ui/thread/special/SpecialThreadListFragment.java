package com.gzsll.hupu.ui.thread.special;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Thread;
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
public class SpecialThreadListFragment extends BaseFragment
        implements SpecialThreadListContract.View, PullToRefreshView.OnRefreshListener,
        LoadMoreRecyclerView.LoadMoreListener {

    public static final int TYPE_RECOMMEND = 1;
    public static final int TYPE_COLLECT = 2;

    public static SpecialThreadListFragment newInstance(int type) {
        SpecialThreadListFragment mFragment = new SpecialThreadListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    Activity mActivity;
    @Inject
    ThreadRecommendPresenter mRecommendPresenter;
    @Inject
    ThreadCollectPresenter mCollectPresenter;

    private SpecialThreadListContract.Presenter mPresenter;

    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PullToRefreshView refreshLayout;

    private int type;

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
        type = bundle.getInt("type");
        mPresenter = (type == TYPE_COLLECT) ? mCollectPresenter : mRecommendPresenter;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);

        mPresenter.attachView(this);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLoadMoreListener(this);
    }

    @Override
    public void initData() {
        mPresenter.onThreadReceive();
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
        mAdapter.bind(threads);
    }

    @Override
    public void onError(String error) {
        setErrorText(error);
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText(type == TYPE_COLLECT ? "没有收藏的帖子" : "没有推荐帖子");
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

    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }
}
