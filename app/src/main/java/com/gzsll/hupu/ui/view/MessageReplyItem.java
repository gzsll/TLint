package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.storage.bean.MessageReply;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/9/8 0008.
 */
@EViewGroup(R.layout.item_list_message_reply)
public class MessageReplyItem extends LinearLayout {
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvTime;
    @ViewById
    TextView tvBoard;
    @ViewById
    TextView tvReply;


    public MessageReplyItem(Context context) {
        super(context);
    }

    public MessageReplyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(MessageReply reply) {
        tvTitle.setText(reply.getThreadInfo().getTitle());
        tvTime.setText(reply.getThreadInfo().getCreateAt());
        tvBoard.setText(reply.getGroupInfo().getGroupName());
        tvReply.setText(reply.getNum() + "");
    }


}
