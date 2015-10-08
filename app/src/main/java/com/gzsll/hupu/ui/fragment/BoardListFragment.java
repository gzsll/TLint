package com.gzsll.hupu.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.BoardListPresenter;
import com.gzsll.hupu.storage.UserStorage;
import com.gzsll.hupu.storage.bean.Boards;
import com.gzsll.hupu.ui.activity.MainActivity;
import com.gzsll.hupu.ui.adapter.BoardListAdapter;
import com.gzsll.hupu.view.BoardListView;
import com.gzsll.hupu.widget.PinnedHeaderListView;
import com.squareup.otto.Bus;

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
    BoardListPresenter boardListPresenter;
    @Inject
    Bus bus;
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
        bus.register(this);
        boardListPresenter.setView(this);
        boardListPresenter.initialize();

        mAdapter.setActivity((MainActivity) getActivity());
        listView.setAdapter(mAdapter);
        boardListPresenter.onBoardListReceive(id);

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
        bus.unregister(this);
    }
}
