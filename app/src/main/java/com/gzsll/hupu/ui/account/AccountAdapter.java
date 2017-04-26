package com.gzsll.hupu.ui.account;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.db.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/10.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onAccountItemDelClicked(User user);

        void onAccountItemClicked(User user);
    }

    private List<User> users = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Inject
    public AccountAdapter() {
    }

    public void bind(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_account, parent, false));
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.ivIcon)
        SimpleDraweeView ivIcon;

        User user;

        @OnClick(R.id.rlDel)
        void rlDelClick() {
            if (onItemClickListener != null) {
                onItemClickListener.onAccountItemDelClicked(user);
            }
        }

        @OnClick(R.id.llItem)
        void llItemClick() {
            if (onItemClickListener != null) {
                onItemClickListener.onAccountItemClicked(user);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
