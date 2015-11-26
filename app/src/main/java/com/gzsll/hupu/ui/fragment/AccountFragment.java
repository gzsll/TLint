package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.gzsll.hupu.presenter.AccountListPresenter;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.adapter.AccountListAdapter;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.view.AccountListItem;
import com.gzsll.hupu.view.AccountListView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/20.
 */
@EFragment
public class AccountFragment extends BaseListFragment<User, AccountListItem> implements AccountListView {

    @Inject
    AccountListAdapter mAdapter;
    @Inject
    AccountListPresenter mAccountListPresenter;
    @Inject
    Bus mBus;

    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @AfterViews
    void init() {
        mBus.register(this);
        mAdapter.setActivity((BaseActivity) getActivity());
        mAccountListPresenter.setView(this);
        mAccountListPresenter.initialize();
    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onRefresh() {
        mAccountListPresenter.refresh();
    }

    @Override
    protected BaseListAdapter<User, AccountListItem> getAdapter() {
        return mAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Subscribe
    public void onAccountChangeEvent(AccountChangeEvent event) {
        mAccountListPresenter.refresh();
    }


}
