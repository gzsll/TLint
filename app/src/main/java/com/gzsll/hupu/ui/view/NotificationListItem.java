package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.bean.Notification;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/12/12.
 */
@EViewGroup(R.layout.item_list_notification)
public class NotificationListItem extends RelativeLayout {
    public NotificationListItem(Context context) {
        super(context);
    }

    public NotificationListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById
    TextView tvNotification;


    public void init(Notification notification) {
        tvNotification.setText(notification.msg);
    }
}
