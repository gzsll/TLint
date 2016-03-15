package com.gzsll.hupu.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.AttendStatusData;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.otto.DelForumAttentionEvent;
import com.gzsll.hupu.ui.activity.ThreadListActivity;
import com.squareup.otto.Bus;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<ForumListAdapter.HeaderViewHolder> {

    Logger logger = Logger.getLogger(ForumListAdapter.class.getSimpleName());

    @Inject
    Activity mActivity;
    @Inject
    Bus mBus;
    @Inject
    ForumApi mForumApi;
    @Inject
    ToastHelper mToastHelper;

    @Inject
    public ForumListAdapter() {
    }


    private List<Forum> forums = new ArrayList<>();
    private String forumId;


    public void bind(List<Forum> forums, String forumId) {
        this.forums = forums;
        this.forumId = forumId;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_forum, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Forum forum = forums.get(position);
        holder.forum = forum;
        if (!TextUtils.isEmpty(forum.getLogo())) {
            holder.ivIcon.setImageURI(Uri.parse(forum.getLogo()));
        }
        holder.tvName.setText(forum.getName());
        holder.swipeLayout.setSwipeEnabled(forumId.equals("0"));
    }

    @Override
    public long getHeaderId(int position) {
        return forums.get(position).getWeight();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.tvCategoryName.setText(forums.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvCategoryName)
        TextView tvCategoryName;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivIcon)
        SimpleDraweeView ivIcon;
        @Bind(R.id.tvName)
        TextView tvName;
        @Bind(R.id.swipeLayout)
        SwipeLayout swipeLayout;

        Forum forum;

        @OnClick(R.id.swipeLayout)
        void swipeLayoutClick() {
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                ThreadListActivity.startActivity(mActivity, forum.getFid());
            }
        }

        @OnClick(R.id.tvDel)
        void tvDelClick() {
            swipeLayout.close();
            mForumApi.delAttention(forum.getFid()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
                @Override
                public void call(AttendStatusData result) {
                    if (result.status == 200 && result.result == 1) {
                        mToastHelper.showToast("取消关注成功");
                        mBus.post(new DelForumAttentionEvent(forum.getFid()));
                        forums.remove(forum);
                        notifyDataSetChanged();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    mToastHelper.showToast("取消关注失败，请重试");
                }
            });
        }


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
