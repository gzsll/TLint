package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.MessageAtPresenter;
import com.gzsll.hupu.storage.bean.MessageAt;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.MessageAtAdapter;
import com.gzsll.hupu.ui.view.MessageAtItem;
import com.gzsll.hupu.view.MessageAtView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8 0008.
 */
@EFragment
public class MessageAtFragment extends BaseListFragment<MessageAt, MessageAtItem> implements MessageAtView {


    @Inject
    MessageAtPresenter mMessageAtPresenter;
    @Inject
    MessageAtAdapter mAdapter;


    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @AfterViews
    void init() {
        mMessageAtPresenter.setView(this);
        mMessageAtPresenter.initialize();
    }

    @Override
    public void onLoadMore() {
        mMessageAtPresenter.onLoadMore();
    }

    @Override
    public void onRefresh() {
        mMessageAtPresenter.onRefresh();
    }

    @Override
    protected BaseListAdapter<MessageAt, MessageAtItem> getAdapter() {
        mAdapter.setActivity((BaseActivity) getActivity());
        return mAdapter;
    }


}
