package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.NewsPresenter;
import com.gzsll.hupu.support.storage.bean.News;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.NewsAdapter;
import com.gzsll.hupu.ui.view.NewsListItem;
import com.gzsll.hupu.view.NewsView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/10/10.
 */
@EFragment
public class NewsListFragment extends BaseListFragment<News, NewsListItem> implements NewsView {

    @Inject
    NewsPresenter mNewsPresenter;
    @Inject
    NewsAdapter mNewsAdapter;

    @AfterViews
    void init() {
        mNewsPresenter.setView(this);
        mNewsPresenter.initialize();
        mNewsPresenter.onNewsReceive(1, "0");
    }

    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @Override
    protected void onLoadMore() {
        mNewsPresenter.onLoadMore();
    }

    @Override
    protected void onRefresh() {
        mNewsPresenter.onRefresh();
    }

    @Override
    protected BaseListAdapter<News, NewsListItem> getAdapter() {
        mNewsAdapter.setActivity((BaseActivity) getActivity());
        return mNewsAdapter;
    }
}
