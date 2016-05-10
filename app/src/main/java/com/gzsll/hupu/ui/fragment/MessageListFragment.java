package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.presenter.MessageListPresenter;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.adapter.MessageListAdapter;
import com.gzsll.hupu.ui.view.MessageListView;
import com.gzsll.hupu.widget.LoadMoreRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class MessageListFragment extends BaseFragment implements MessageListView, PullToRefreshView.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener {

    @Bind(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    PullToRefreshView refreshLayout;


    @Inject
    MessageListAdapter mAdapter;
    @Inject
    MessageListPresenter mPresenter;
    @Inject
    Activity mActivity;


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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLoadMoreListener(this);
    }

    @Override
    public void initData() {
        mPresenter.onMessageListReceive();
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
    public void renderMessageList(List<Message> messages) {
        mAdapter.bind(messages);
    }

    @Override
    public void onRefreshCompleted() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadCompleted(boolean haMore) {
        recyclerView.onLoadCompleted(haMore);
    }

    @Override
    public void onError() {
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText("暂无论坛消息");
        showEmpty(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }
}
