package com.gzsll.hupu.ui.forum;

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
import com.gzsll.hupu.db.Forum;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/11.
 */
public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<ForumListAdapter.HeaderViewHolder> {


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onForumDelAttentionClick(Forum forum);

        void onForumOfflineClick(Forum forum);

        void onForumClick(Forum forum);
    }

    private List<Forum> forums = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Inject
    public ForumListAdapter() {
    }

    public void bind(List<Forum> forums) {
        this.forums = forums;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_forum, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Forum forum = forums.get(position);
        holder.forum = forum;
        if (!TextUtils.isEmpty(forum.getLogo())) {
            holder.ivIcon.setImageURI(Uri.parse(forum.getLogo()));
        }
        holder.tvName.setText(forum.getName());
        holder.swipeLayout.setSwipeEnabled(forum.getForumId().equals("0"));
    }

    @Override
    public long getHeaderId(int position) {
        return forums.get(position).getWeight();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.category_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.tvCategoryName.setText(forums.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }

    public void remove(Forum forum) {
        forums.remove(forum);
        notifyDataSetChanged();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivIcon)
        SimpleDraweeView ivIcon;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.swipeLayout)
        SwipeLayout swipeLayout;

        Forum forum;

        @OnClick(R.id.swipeLayout)
        void swipeLayoutClick() {
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                if (onItemClickListener != null) {
                    onItemClickListener.onForumClick(forum);
                }
            }
        }

        @OnClick(R.id.tvDel)
        void tvDelClick() {
            swipeLayout.close();
            if (onItemClickListener != null) {
                onItemClickListener.onForumDelAttentionClick(forum);
            }
        }

        @OnClick(R.id.tvOffline)
        void tvOfflineClick() {
            swipeLayout.close();
            if (onItemClickListener != null) {
                onItemClickListener.onForumOfflineClick(forum);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
