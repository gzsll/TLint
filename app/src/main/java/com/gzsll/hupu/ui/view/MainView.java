package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/17.
 */
public interface MainView extends BaseView {


    void renderUserInfo(User user);


    void renderAccountList(List<User> users, String[] items);

    void renderNotification(int count);


    void reload();


}
