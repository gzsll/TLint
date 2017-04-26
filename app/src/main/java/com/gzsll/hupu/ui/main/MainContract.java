package com.gzsll.hupu.ui.main;

import android.app.Fragment;
import android.view.MenuItem;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface MainContract {
    interface View extends BaseView {
        void setTitle(CharSequence title);

        void renderUserInfo(User user);

        void renderAccountList(List<User> users, String[] items);

        void renderNotification(int count);

        void reload();

        void closeDrawers();

        void showFragment(Fragment fragment);

        void showMessageUi();

        void showUserProfileUi(String uid);

        void showLoginUi();

        void showAccountUi();

        void showSettingUi();

        void showFeedBackUi();

        void showAboutUi();
    }

    interface Presenter extends BasePresenter<View> {

        void onNightModelClick();

        void onNotificationClick();

        void onCoverClick();

        void onNavigationClick(MenuItem item);

        void showAccountMenu();

        void onAccountItemClick(int position, List<User> users, final String[] items);

        void exist();

        boolean isLogin();
    }
}
