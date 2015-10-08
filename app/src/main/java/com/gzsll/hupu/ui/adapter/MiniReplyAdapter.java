package com.gzsll.hupu.ui.adapter;

import android.view.ViewGroup;

import com.gzsll.hupu.storage.UserStorage;
import com.gzsll.hupu.storage.bean.MiniReplyListItem;
import com.gzsll.hupu.ui.view.MiniReplyItem;
import com.gzsll.hupu.ui.view.MiniReplyItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/8/21.
 */
public class MiniReplyAdapter extends BaseRecyclerViewAdapter<MiniReplyListItem, MiniReplyItem> {

    @Inject
    UserStorage mUserStorage;

    private int floorer;

    @Override
    protected MiniReplyItem onCreateItemView(ViewGroup parent, int viewType) {
        return MiniReplyItem_.build(mActivity);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MiniReplyItem> holder, int position) {
        MiniReplyItem itemView = holder.getView();
        itemView.mUserStorage = mUserStorage;
        itemView.mActivity = mActivity;
        MiniReplyListItem item = items.get(position);
        itemView.initReply(item, floorer, position);
    }

    public void setFloorer(int floorer) {
        this.floorer = floorer;
    }
}
