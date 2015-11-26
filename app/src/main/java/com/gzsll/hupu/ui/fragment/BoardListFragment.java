package com.gzsll.hupu.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.DelGroupAttentionEvent;
import com.gzsll.hupu.otto.StartOfflineEvent;
import com.gzsll.hupu.presenter.BoardListPresenter;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Boards;
import com.gzsll.hupu.ui.activity.MainActivity;
import com.gzsll.hupu.ui.adapter.BoardListAdapter;
import com.gzsll.hupu.view.BoardListView;
import com.gzsll.hupu.widget.PinnedHeaderListView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/15.
 */
@EFragment
public class BoardListFragment extends BaseFragment implements BoardListView {

    Logger logger = Logger.getLogger(BoardListFragment.class.getSimpleName());

    @FragmentArg
    int id;

    @Inject
    BoardListAdapter mAdapter;
    @Inject
    BoardListPresenter mBoardListPresenter;
    @Inject
    Bus mBus;
    @Inject
    UserStorage mUserStorage;

    @ViewById
    PinnedHeaderListView listView;


    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_board, null);
    }

    @AfterViews
    void init() {
        mBus.register(this);
        mBoardListPresenter.setView(this);
        mBoardListPresenter.initialize();

        mAdapter.setActivity((MainActivity) getActivity());
        listView.setAdapter(mAdapter);
        mBoardListPresenter.onBoardListReceive(id);

    }


    @Override
    public void onError() {
        showError(true);
    }

    @Override
    public void renderBoardList(List<Boards> boardGroups) {
        mAdapter.bindData(boardGroups);
    }


    @Override
    public void showLoginView() {

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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Subscribe
    public void onStartOfflineEvent(StartOfflineEvent event) {
        if (event.getBoard() != null) {
            mBoardListPresenter.offlineGroup(event.getBoard());
        } else {
            mBoardListPresenter.offlineGroups();
        }
    }

    @Subscribe
    public void onDelGroupAttentionEvent(DelGroupAttentionEvent event) {
        mBoardListPresenter.delGroupAttention(event.getGroupId());
    }

    @Override
    public void onReloadClicked() {
        mBoardListPresenter.onReload();
    }
}
