package com.gzsll.hupu.presenter;

import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.view.AccountListView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/20.
 */
public class AccountListPresenter extends Presenter<AccountListView> {

    @Inject
    UserDao mUserDao;

    @Override
    public void initialize() {
        view.showLoading();
        loadUserList();
        view.hideLoading();
    }

    private void loadUserList() {
        List<User> users = mUserDao.queryBuilder().list();
        view.renderList(users);
    }

    public void refresh() {
        loadUserList();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
