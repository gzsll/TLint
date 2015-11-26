package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.gzsll.hupu.support.storage.bean.MessageAt;
import com.gzsll.hupu.ui.activity.ReplyDetailActivity_;
import com.gzsll.hupu.ui.view.MessageAtItem;
import com.gzsll.hupu.ui.view.MessageAtItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8.
 */
public class MessageAtAdapter extends BaseListAdapter<MessageAt, MessageAtItem> {

    @Inject
    public MessageAtAdapter() {
    }


    @Override
    protected MessageAtItem onCreateItemView(Context context) {
        return MessageAtItem_.build(context);
    }

    @Override
    protected void onBindView(MessageAtItem view, int position, final MessageAt data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyDetailActivity_.intent(mActivity).replyItem(data.getReply()).start();
            }
        });
        view.init(data);
    }
}
