package com.gzsll.hupu.ui.adapter;

import android.content.Context;

import com.gzsll.hupu.support.storage.bean.Notification;
import com.gzsll.hupu.ui.view.NotificationListItem;
import com.gzsll.hupu.ui.view.NotificationListItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
public class NotificationAdapter extends BaseListAdapter<Notification, NotificationListItem> {

    @Inject
    public NotificationAdapter() {
    }

    @Override
    protected NotificationListItem onCreateItemView(Context context) {
        return NotificationListItem_.build(context);
    }

    @Override
    protected void onBindView(NotificationListItem view, int position, final Notification data) {
        view.init(data);
    }
}
