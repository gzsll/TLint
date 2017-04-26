package com.gzsll.hupu.ui.pmdetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/5/6.
 */
public class PmDetailFragment extends BaseFragment
        implements PmDetailContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static PmDetailFragment newInstance(String uid) {
        PmDetailFragment mFragment = new PmDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.etContent)
    EditText etContent;

    @Inject
    PmDetailAdapter mAdapter;
    @Inject
    PmDetailPresenter mPresenter;
    @Inject
    Activity mActivity;

    private String uid;
    private MenuItem blockItem;

    @Override
    public void initInjector() {
        getComponent(PmDetailComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_pm_detail;
    }

    @Override
    public void getBundle(Bundle bundle) {
        uid = bundle.getString("uid");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(this);
        mPresenter.attachView(this);
    }

    @Override
    public void initData() {
        mPresenter.onPmDetailReceive();
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
    public void isBlock(boolean isBlock) {
        if (blockItem != null) {
            blockItem.setTitle(isBlock ? "取消屏蔽" : "屏蔽该用户");
        }
    }

    @Override
    public void renderPmDetailList(List<PmDetail> pmDetails) {
        mAdapter.bind(pmDetails);
    }

    @Override
    public void scrollTo(int position) {
        recyclerView.scrollToPosition(position);
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
    public void cleanEditText() {
        etContent.setText("");
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

    @OnClick(R.id.btSend)
    void btSendClick() {
        mPresenter.send(etContent.getText().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pm_detail, menu);
        blockItem = menu.findItem(R.id.block);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toUser:
                UserProfileActivity.startActivity(mActivity, uid);
                break;
            case R.id.clear:
                mPresenter.clear();
                break;
            case R.id.block:
                mPresenter.block();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
