package com.gzsll.hupu.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.view.BaseListView;
import com.gzsll.hupu.widget.SwipyRefreshLayout;
import com.gzsll.hupu.widget.SwipyRefreshLayoutDirection;

import java.util.List;

/**
 * Created by sll on 2015/9/17.
 */
public abstract class BaseListFragment<T, V extends View> extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener, BaseListView<T> {


    private SwipyRefreshLayout refreshLayout;
    private ListView listView;

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        if (inflateContentView() > 0) {
            View view = inflater.inflate(inflateContentView(), null);
            refreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.refreshLayout);
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(getAdapter());
            refreshLayout.setOnRefreshListener(this);
            return view;
        } else {
            return super.onCreateContentView(inflater);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            onLoadMore();
        } else {
            onRefresh();
        }
    }

    protected abstract int inflateContentView();

    protected abstract void onLoadMore();

    protected abstract void onRefresh();

    protected abstract BaseListAdapter<T, V> getAdapter();

    @Override
    public void renderList(List<T> list) {
        refreshLayout.setRefreshing(false);
        getAdapter().updateItems(list);
    }

    @Override
    public void onError(String error) {
        showError(true);
    }

    @Override
    public void onEmpty() {
        showEmpty(true);
    }

    @Override
    public void onScrollToTop() {
        listView.smoothScrollToPosition(0);
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
