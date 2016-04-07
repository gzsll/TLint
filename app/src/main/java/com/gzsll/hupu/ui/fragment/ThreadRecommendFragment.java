package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.presenter.ThreadRecommendPresenter;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.adapter.ThreadListAdapter;
import com.gzsll.hupu.ui.view.ThreadRecommendView;
import com.gzsll.hupu.widget.LoadMoreRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class ThreadRecommendFragment extends BaseFragment implements ThreadRecommendView, PullToRefreshView.OnRefreshListener {

    private static Logger logger = Logger.getLogger(ThreadRecommendFragment.class.getSimpleName());

    public static ThreadRecommendFragment newInstance() {
        return new ThreadRecommendFragment();
    }


    @Inject
    ThreadRecommendPresenter mPresenter;
    @Inject
    ThreadListAdapter mAdapter;
    @Inject
    Activity mActivity;


    @Bind(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    PullToRefreshView refreshLayout;




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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        });

    }

    @Override
    public void initData() {
        mPresenter.onRecommendThreadsReceive();
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
    public void onLoadCompleted(boolean hasMore) {
        recyclerView.notifyMoreFinish(hasMore);
    }

    @Override
    public void onRefreshCompleted() {
        refreshLayout.setRefreshing(false);
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
    public void onReloadClicked() {
        mPresenter.onReload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }
}
