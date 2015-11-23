package com.gzsll.hupu.ui.adapter;

import android.content.Context;

import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.ui.view.AccountListItem;
import com.gzsll.hupu.ui.view.AccountListItem_;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/20.
 */
public class AccountListAdapter extends BaseListAdapter<User, AccountListItem> {
    @Inject
    UserStorage mUserStorage;
    @Inject
    UserDao mUserDao;
    @Inject
    Bus mBus;

    @Override
    protected AccountListItem onCreateItemView(Context context) {
        AccountListItem item = AccountListItem_.build(context);
        item.mUserDao = mUserDao;
        item.mUserStorage = mUserStorage;
        item.mBus = mBus;
        return item;
    }

    @Override
    protected void onBindView(AccountListItem view, int position, User data) {
        view.init(data);
    }
}
