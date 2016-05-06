package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.presenter.PmDetailPresenter;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.adapter.PmDetailAdapter;
import com.gzsll.hupu.ui.view.PmDetailView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/5/6.
 */
public class PmDetailFragment extends BaseFragment implements PmDetailView, SwipeRefreshLayout.OnRefreshListener {

    public static PmDetailFragment newInstance(String uid) {
        PmDetailFragment mFragment = new PmDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    PmDetailAdapter mAdapter;
    @Inject
    PmDetailPresenter mPresenter;
    @Inject
    Activity mActivity;


    private String uid;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.base_list_layout;
    }

    @Override
    public void getBundle(Bundle bundle) {
        uid = bundle.getString("uid");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        mPresenter.onPmDetailReceive(uid);
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
    public void renderPmDetailList(List<PmDetail> pmDetails) {
        mAdapter.bind(pmDetails);

    }

    @Override
    public void onRefreshCompleted() {
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onError() {
        showError(true);
    }

    @Override
    public void onEmpty() {
        setEmptyText("暂时没有相关短消息");
        showEmpty(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.onLoadMore();
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
}
