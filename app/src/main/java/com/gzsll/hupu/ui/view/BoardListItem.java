package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.db.Board;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/6/16.
 */
@EViewGroup(R.layout.item_list_board)
public class BoardListItem extends LinearLayout {
    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvName;

    public BoardListItem(Context context) {
        super(context);
    }

    public BoardListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Board board) {
        ivIcon.setImageURI(Uri.parse(board.getBoardIcon()));
        tvName.setText(board.getBoardName());
    }
}
