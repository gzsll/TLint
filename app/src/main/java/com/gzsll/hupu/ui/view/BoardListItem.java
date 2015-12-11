package com.gzsll.hupu.ui.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.DelGroupAttentionEvent;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Board;
import com.gzsll.hupu.ui.activity.LoginActivity_;
import com.gzsll.hupu.ui.activity.ThreadActivity_;
import com.squareup.otto.Bus;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

/**
 * Created by sll on 2015/6/16.
 */
@EViewGroup(R.layout.item_list_board)
public class BoardListItem extends LinearLayout {
    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvName;
    @ViewById
    SwipeLayout swipeLayout;

    public Bus mBus;
    public UserStorage mUserStorage;
    public Activity mActivity;
    private Board board;

    private Logger logger = Logger.getLogger(BoardListItem.class.getSimpleName());

    public BoardListItem(Context context) {
        super(context);
    }

    public BoardListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Board board) {
        this.board = board;
        ivIcon.setImageURI(Uri.parse(board.logo));
        tvName.setText(board.name);
    }

    @Click
    void tvDel() {
        swipeLayout.close();
        mBus.post(new DelGroupAttentionEvent(board.fid));
    }


    @Click
    void swipeLayout() {
        if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
            if (mUserStorage.isLogin()) {
                ThreadActivity_.intent(mActivity).fid(board.fid).start();
            } else {
                LoginActivity_.intent(mActivity).start();
            }
        }
    }


    @Click
    void tvOffline() {
        swipeLayout.close();
        // mBus.post(new StartOfflineEvent(board));
    }
}
