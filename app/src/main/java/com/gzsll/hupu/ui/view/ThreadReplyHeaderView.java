package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/6/18.
 */
@EViewGroup(R.layout.reply_header_view)
public class ThreadReplyHeaderView extends RelativeLayout {
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvPage;
    @ViewById
    TextView tvJump;

    public ThreadReplyHeaderView(Context context) {
        super(context);
    }

    public ThreadReplyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setPage(int current, int total) {
        tvPage.setText("第" + current + "/" + total + "页");
    }

    @Click
    void llJump() {
        if (onJumpClick != null) {
            onJumpClick.onClick();
        }
    }

    public void setOnJumpClick(OnJumpClick onJumpClick) {
        this.onJumpClick = onJumpClick;
    }

    public interface OnJumpClick {
        void onClick();
    }

    private OnJumpClick onJumpClick;

    public void setPageVisibility(int visibility) {
        tvPage.setVisibility(visibility);
    }

    public void setJumpVisibility(int visibility) {
        tvJump.setVisibility(visibility);
    }


}
