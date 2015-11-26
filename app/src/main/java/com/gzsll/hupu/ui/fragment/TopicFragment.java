package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.TopicPresenter;
import com.gzsll.hupu.support.storage.bean.Topic;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.LoginActivity_;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.TopicListAdapter;
import com.gzsll.hupu.ui.view.TopicListItem;
import com.gzsll.hupu.view.TopicView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import javax.inject.Inject;

/**
 * Created by sll on 2015/5/28.
 */
@EFragment
public class TopicFragment extends BaseListFragment<Topic, TopicListItem> implements TopicView {

    @FragmentArg
    int type;
    @FragmentArg
    String uid;


    @Inject
    TopicPresenter mTopicPresenter;
    @Inject
    TopicListAdapter adapter;


    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @AfterViews
    void init() {
        mTopicPresenter.setView(this);
        mTopicPresenter.initialize();
        mTopicPresenter.onTopicReceive(type, uid);

    }


    @Override
    public void showLoginView() {
        LoginActivity_.intent(this).start();
    }


    @Override
    public void onRefresh() {
        mTopicPresenter.onRefresh();
    }

    @Override
    protected BaseListAdapter<Topic, TopicListItem> getAdapter() {
        adapter.setActivity((BaseActivity) getActivity());
        return adapter;
    }

    @Override
    public void onLoadMore() {
        mTopicPresenter.onLoadMore();
    }

    @Override
    public void onReloadClicked() {
        mTopicPresenter.onReload();
    }
}
