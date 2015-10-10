package com.gzsll.hupu.view;

import com.gzsll.hupu.support.storage.bean.Notification;

import java.util.List;

/**
 * Created by sll on 2015/5/28.
 */
public interface NotificationView extends BaseView {
    void renderNotification(List<Notification> notifications);

    void showNotificationCount(int count);


}
