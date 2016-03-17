package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/10.
 */
public interface AccountView extends BaseView {



    void renderUserList(List<User> users);


}
