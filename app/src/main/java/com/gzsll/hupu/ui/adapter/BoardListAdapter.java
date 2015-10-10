package com.gzsll.hupu.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Boards;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.LoginActivity_;
import com.gzsll.hupu.ui.activity.ThreadActivity_;
import com.gzsll.hupu.ui.view.BoardListItem;
import com.gzsll.hupu.ui.view.BoardListItem_;
import com.gzsll.hupu.ui.view.CategoryHeaderView;
import com.gzsll.hupu.ui.view.CategoryHeaderView_;
import com.gzsll.hupu.widget.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/16.
 */
public class BoardListAdapter extends SectionedBaseAdapter {


    @Inject
    UserStorage mUserStorage;

    private List<Boards> boardsList = new ArrayList<>();
    private BaseActivity mActivity;

    public void bindData(List<Boards> boardsList) {
        this.boardsList = boardsList;
        notifyDataSetChanged();
    }


    @Override
    public Board getItem(int section, int position) {
        if (boardsList.isEmpty()) {
            return null;
        } else {
            return boardsList.get(section).getBoards().get(position);
        }
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return boardsList.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (boardsList.isEmpty()) {
            return 0;
        } else {
            return boardsList.get(section).getBoards().size();
        }
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        BoardListItem view;
        if (convertView == null) {
            view = BoardListItem_.build(mActivity);
        } else {
            view = (BoardListItem) convertView;
        }
        final Board board = getItem(section, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserStorage.isLogin()) {
                    ThreadActivity_.intent(mActivity).mGroupId(board.getBoardId()).start();
                } else {
                    LoginActivity_.intent(mActivity).start();
                }
            }
        });
        view.init(board);
        return view;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        CategoryHeaderView view = CategoryHeaderView_.build(mActivity);
        view.setTitleText(boardsList.get(section).getName());
        return view;
    }

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
