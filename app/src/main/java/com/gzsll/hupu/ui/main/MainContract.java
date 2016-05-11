package com.gzsll.hupu.ui.main;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface MainContract {
    interface View extends BaseView {
        void renderUserInfo(User user);


        void renderAccountList(List<User> users, String[] items);

        void renderNotification(int count);

        void reload();
    }

    interface Presenter extends BasePresenter<View> {
        void clickNotification();

        void toLogin();

        void clickCover();

        void showAccountMenu();

        void onAccountItemClick(int position, List<User> users, final String[] items);

        void exist();

        boolean isLogin();
    }
}
