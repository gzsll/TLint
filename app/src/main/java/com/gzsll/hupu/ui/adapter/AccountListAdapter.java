package com.gzsll.hupu.ui.adapter;

import android.content.Context;

import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.ui.view.AccountListItem;
import com.gzsll.hupu.ui.view.AccountListItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/20.
 */
public class AccountListAdapter extends BaseListAdapter<User,AccountListItem> {

    @Inject
    public AccountListAdapter(){}

    @Override
    protected AccountListItem onCreateItemView(Context context) {
        return AccountListItem_.build(context);
    }

    @Override
    protected void onBindView(AccountListItem view, int position, User data) {
          view.init(data);
    }
}
