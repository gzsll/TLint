package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.squareup.otto.Bus;

import org.androidannotations.annotations.Click;
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
    TextView tvName, tvDesc;
    @ViewById
    SimpleDraweeView ivIcon;

    public UserStorage mUserStorage;
    public UserDao mUserDao;
    public Bus mBus;

    private User user;

    public void init(User user) {
        this.user = user;
        if(!TextUtils.isEmpty(user.getIcon())){
            ivIcon.setImageURI(Uri.parse(user.getIcon()));
        }
        tvName.setText(user.getUserName());
//        tvDesc.setText(String.format("%1s,%2d级", user.getSex() == 0 ? "男" : "女", user.getLevel()));
    }


    @Click
    void rlDel() {
        new MaterialDialog.Builder(getContext()).title("提示").content("确认删除账号?").positiveText("确定").negativeText("取消").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                mUserDao.delete(user);
                if (String.valueOf(user.getId()).equals(mUserStorage.getUid())) {
                    mUserStorage.logout();
                }
                mBus.post(new AccountChangeEvent());
            }
        }).show();
    }
}
