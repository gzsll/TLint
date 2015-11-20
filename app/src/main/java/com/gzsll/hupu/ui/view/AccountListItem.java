package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.db.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/11/20.
 */
@EViewGroup(R.layout.item_list_account)
public class AccountListItem extends LinearLayout {
    public AccountListItem(Context context) {
        super(context);
    }

    public AccountListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById
    TextView tvName,tvDesc;
    @ViewById
    SimpleDraweeView ivIcon;

    public void init(User user){

    }
}
