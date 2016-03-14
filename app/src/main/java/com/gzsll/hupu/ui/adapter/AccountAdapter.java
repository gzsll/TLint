package com.gzsll.hupu.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/10.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {


    @Inject
    UserStorage mUserStorage;
    @Inject
    Activity mActivity;
    @Inject
    UserDao mUserDao;
    @Inject
    Bus mBus;

    @Inject
    public AccountAdapter() {
    }

    private List<User> users = new ArrayList<>();

    public void bind(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_account, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.user = user;
        if (!TextUtils.isEmpty(user.getIcon())) {
            holder.ivIcon.setImageURI(Uri.parse(user.getIcon()));
        }
        holder.tvName.setText(user.getUserName());
        holder.tvDesc.setText(user.getRegisterTime());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;
        @Bind(R.id.tvDesc)
        TextView tvDesc;
        @Bind(R.id.ivIcon)
        SimpleDraweeView ivIcon;

        User user;


        @OnClick(R.id.rlDel)
        void rlDelClick() {
            new MaterialDialog.Builder(mActivity).title("提示").content("确认删除账号?").positiveText("确定").negativeText("取消").callback(new MaterialDialog.ButtonCallback() {
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


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
