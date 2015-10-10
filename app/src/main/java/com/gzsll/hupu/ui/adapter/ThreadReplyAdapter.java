package com.gzsll.hupu.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.otto.ReplyJumpClickEvent;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItems;
import com.gzsll.hupu.support.utils.ReplyViewHelper;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.ReplyDetailActivity_;
import com.gzsll.hupu.ui.view.ThreadReplyHeaderView;
import com.gzsll.hupu.ui.view.ThreadReplyHeaderView_;
import com.gzsll.hupu.ui.view.ThreadReplyItemView;
import com.gzsll.hupu.ui.view.ThreadReplyItemView_;
import com.gzsll.hupu.widget.SectionedBaseAdapter;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/18.
 */
public class ThreadReplyAdapter extends SectionedBaseAdapter {

    @Inject
    ThreadApi mThreadApi;
    @Inject
    ReplyViewHelper mReplyViewHelper;
    @Inject
    UserStorage mUserStorage;
    @Inject
    Bus mBus;

    private BaseActivity mActivity;
    private List<ThreadReplyItems> replyItems = new ArrayList<>();

    public void bindData(List<ThreadReplyItems> replyItems) {
        this.replyItems = replyItems;
        notifyDataSetChanged();
    }


    @Override
    public ThreadReplyItem getItem(int section, int position) {
        if (replyItems.isEmpty()) {
            return null;
        } else {
            return replyItems.get(section).getmLists().get(position);
        }
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return replyItems.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (replyItems.isEmpty()) {
            return 0;
        } else {
            return replyItems.get(section).getmLists().size();
        }
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ThreadReplyItemView view;
        if (convertView == null) {
            view = ThreadReplyItemView_.build(mActivity);
            view.mThreadApi = mThreadApi;
            view.mUserStorage = mUserStorage;
            view.mReplyViewHelper = mReplyViewHelper;
            view.mActivity = mActivity;
        } else {
            view = (ThreadReplyItemView) convertView;
        }
        final ThreadReplyItem item = getItem(section, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyDetailActivity_.intent(mActivity).replyItem(item).start();
            }
        });
        view.init(item);
        return view;
    }

    @Override
    public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = ThreadReplyHeaderView_.build(mActivity);
            }
            ThreadReplyHeaderView head_item = (ThreadReplyHeaderView) convertView;
            if (replyItems.size() > 0) {
                String name = replyItems.get(section).getName();
                head_item.setTitle(name);
                if (name.contains("亮了")) {
                    head_item.setJumpVisibility(View.GONE);
                    head_item.setPageVisibility(View.GONE);
                } else {
                    head_item.setJumpVisibility(View.VISIBLE);
                    head_item.setPageVisibility(View.VISIBLE);
                    head_item.setPage(replyItems.get(section).getCurrentPage(), replyItems.get(section).getTotalPage());
                    head_item.setOnJumpClick(new ThreadReplyHeaderView.OnJumpClick() {
                        @Override
                        public void onClick() {
                            mBus.post(new ReplyJumpClickEvent(replyItems.get(section).getCurrentPage()));
                        }
                    });
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
