package com.gzsll.hupu.ui.account;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface AccountContract {
    interface View extends BaseView {
        void renderUserList(List<User> users);
    }

    interface Presenter extends BasePresenter<View> {
        void onAccountDelClick(User user);

        void onAccountClick(User user);
    }
}
