package com.gzsll.hupu.ui.pmlist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.messagelist.MessageComponent;
import com.gzsll.hupu.ui.pmdetail.PmDetailActivity;
import com.gzsll.hupu.widget.LoadMoreRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/5/6.
 */
public class PmListFragment extends BaseFragment
        implements PmListContract.View, PullToRefreshView.OnRefreshListener,
        LoadMoreRecyclerView.LoadMoreListener, PmListAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PullToRefreshView refreshLayout;

    @Inject
    PmListPresenter mPresenter;
    @Inject
    PmListAdapter mAdapter;

    @Override
    public void initInjector() {
        getComponent(MessageComponent.class).inject(this);
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
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        mPresenter.onPmListReceive();
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
    public void renderPmList(List<Pm> pms) {
        mAdapter.bind(pms);
        recyclerView.getAdapter().notifyDataSetChanged();
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
        setEmptyText("暂无历史消息");
        showEmpty(true);
    }

    @Override
    public void showPmDetailUi(String uid, String name) {
        PmDetailActivity.startActivity(getActivity(), uid, name);
    }

    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
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
    public void onPmListClick(Pm pm) {
        mPresenter.onPmListClick(pm);
    }
}
