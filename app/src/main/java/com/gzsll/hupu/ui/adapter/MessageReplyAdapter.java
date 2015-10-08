package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.gzsll.hupu.storage.bean.MessageReply;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.view.MessageReplyItem;
import com.gzsll.hupu.ui.view.MessageReplyItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageReplyAdapter extends BaseListAdapter<MessageReply, MessageReplyItem> {

    @Inject
    public MessageReplyAdapter() {
    }


    @Override
    protected MessageReplyItem onCreateItemView(Context context) {
        return MessageReplyItem_.build(mActivity);
    }

    @Override
    protected void onBindView(MessageReplyItem view, int position, final MessageReply data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentActivity_.intent(mActivity).mThreadId(data.getThreadInfo().getId()).start();
            }
        });
        view.init(data);
    }
}
