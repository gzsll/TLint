package com.gzsll.hupu.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.ui.activity.UserProfileActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/5/6.
 */
public class PmDetailAdapter extends RecyclerView.Adapter<PmDetailAdapter.ViewHolder> {


    @Inject
    UserStorage mUserStorage;
    @Inject
    Activity mActivity;

    @Inject
    public PmDetailAdapter() {
    }

    private static final int TYPE_USER = 1;
    private static final int TYPE_OTHER = 2;

    private List<PmDetail> mPmDetails = new ArrayList<>();

    public void bind(List<PmDetail> pmDetails) {
        mPmDetails = pmDetails;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        PmDetail detail = mPmDetails.get(position);
        if (detail.puid.equals(mUserStorage.getUid())) {
            return TYPE_USER;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public PmDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pm_user, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pm_other, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PmDetail detail = mPmDetails.get(position);
        holder.detail = detail;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(Long.valueOf(detail.create_time) * 1000);
        holder.tvDate.setText(format.format(date));
        if (!TextUtils.isEmpty(detail.header)) {
            holder.ivUser.setImageURI(Uri.parse(detail.header));
        }
        holder.tvContent.setText(detail.content);
    }

    @Override
    public int getItemCount() {
        return mPmDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        PmDetail detail;

        @Bind(R.id.tvDate)
        TextView tvDate;
        @Bind(R.id.ivUser)
        SimpleDraweeView ivUser;
        @Bind(R.id.pbReply)
        ProgressBar pbReply;
        @Bind(R.id.tvContent)
        TextView tvContent;

        @OnClick(R.id.ivUser)
        void ivUserClick() {
            UserProfileActivity.startActivity(mActivity, detail.puid);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
