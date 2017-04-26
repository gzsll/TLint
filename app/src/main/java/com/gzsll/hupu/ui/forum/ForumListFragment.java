package com.gzsll.hupu.ui.forum;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.main.MainComponent;
import com.gzsll.hupu.ui.thread.list.ThreadListActivity;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class ForumListFragment extends BaseFragment
        implements ForumListContract.View, ForumListAdapter.OnItemClickListener {

    public static ForumListFragment newInstance(String forumId) {
        ForumListFragment mFragment = new ForumListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("forumId", forumId);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private String forumId;

    @Inject
    ForumListPresenter mPresenter;
    @Inject
    ForumListAdapter mAdapter;

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.base_list_layout;
    }

    @Override
    public void getBundle(Bundle bundle) {
        forumId = bundle.getString("forumId");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        final StickyRecyclerHeadersDecoration headersDecor =
                new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);
        refreshLayout.setEnabled(false);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        mPresenter.onForumListReceive(forumId);
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
    public void onError() {
        showError(true);
    }

    @Override
    public void renderForumList(List<Forum> forumList) {
        mAdapter.bind(forumList);
    }

    @Override
    public void showThreadUi(String fid) {
        ThreadListActivity.startActivity(getActivity(), fid);
    }

    @Override
    public void removeForum(Forum forum) {
        mAdapter.remove(forum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onForumDelAttentionClick(Forum forum) {
        mPresenter.onForumAttentionDelClick(forum);
    }

    @Override
    public void onForumOfflineClick(Forum forum) {
        mPresenter.onForumOfflineClick(forum);
    }

    @Override
    public void onForumClick(Forum forum) {
        mPresenter.onForumClick(forum);
    }
}
