package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.MessageReplyPresenter;
import com.gzsll.hupu.support.storage.bean.MessageReply;
import com.gzsll.hupu.ui.activity.NoticeActivity;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.MessageReplyAdapter;
import com.gzsll.hupu.ui.view.MessageReplyItem;
import com.gzsll.hupu.view.MessageReplyView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8 0008.
 */
@EFragment
public class MessageReplyFragment extends BaseListFragment<MessageReply, MessageReplyItem> implements MessageReplyView {


    @Inject
    MessageReplyPresenter mMessageReplyPresenter;
    @Inject
    MessageReplyAdapter mAdapter;


    private NoticeActivity mActivity;


    @AfterViews
    void init() {
        mActivity = (NoticeActivity) getActivity();
        mAdapter.setActivity(mActivity);
        mMessageReplyPresenter.setView(this);
        mMessageReplyPresenter.initialize();
    }


    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @Override
    protected void onLoadMore() {
        mMessageReplyPresenter.onLoadMore();
    }

    @Override
    protected void onRefresh() {
        mMessageReplyPresenter.onRefresh();
    }

    @Override
    protected BaseListAdapter<MessageReply, MessageReplyItem> getAdapter() {
        return mAdapter;
    }

    @Override
    public void onReloadClicked() {
        mMessageReplyPresenter.onReload();
    }
}
