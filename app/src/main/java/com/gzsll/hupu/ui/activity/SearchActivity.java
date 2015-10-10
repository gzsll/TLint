package com.gzsll.hupu.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.SearchPresenter;
import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.Info;
import com.gzsll.hupu.ui.adapter.BaseRecyclerViewAdapter;
import com.gzsll.hupu.ui.adapter.ThreadListAdapter;
import com.gzsll.hupu.view.ThreadListView;
import com.gzsll.hupu.widget.SwipeRefreshLoadLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/1.
 */
@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements ThreadListView, SwipeRefreshLoadLayout.OnRefreshListener, SwipeRefreshLoadLayout.OnLoadMoreListener {

    @Extra
    int mFid;


    @ViewById
    EditText etSearch;
    @ViewById
    SwipeRefreshLoadLayout refreshLayout;
    @ViewById
    RecyclerView recyclerView;


    @Inject
    SearchPresenter searchPresenter;
    @Inject
    ThreadListAdapter adapter;

    @AfterViews
    void init() {
        searchPresenter.setView(this);
        searchPresenter.initialize();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {
                searchPresenter.onItemSelected(item);
            }
        });
    }


    @Override
    public void renderThreadInfo(Info info) {

    }

    @Override
    public void renderThreads(List<GroupThread> threads) {

    }

    @Override
    @UiThread
    public void onError(String error) {

    }

    @Override
    @UiThread
    public void onEmpty() {

    }

    @Override
    @UiThread
    public void onScrollToTop() {
        refreshLayout.setRefreshing(true);
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void showLoginView() {

    }

    @Override
    @UiThread
    public void showLoading() {

    }

    @Override
    @UiThread
    public void hideLoading() {

    }

    @Override
    @UiThread
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        searchPresenter.onLoadMore();
    }

    @Override
    public void onRefresh() {
    }


    @Click
    void rlSearch() {
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }
}
