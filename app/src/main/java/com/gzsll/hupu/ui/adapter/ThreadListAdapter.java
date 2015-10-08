package com.gzsll.hupu.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.gzsll.hupu.storage.bean.GroupThread;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.view.ThreadListItem;
import com.gzsll.hupu.ui.view.ThreadListItem_;
import com.gzsll.hupu.utils.FormatHelper;
import com.gzsll.hupu.utils.SettingPrefHelper;

import javax.inject.Inject;

/**
 * Created by sll on 2015/3/7.
 */
public class ThreadListAdapter extends BaseRecyclerViewAdapter<GroupThread, ThreadListItem> {


    @Inject
    FormatHelper formatHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    @Override
    protected ThreadListItem onCreateItemView(ViewGroup parent, int viewType) {
        return ThreadListItem_.build(mActivity);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ThreadListItem> forumListItemViewWrapper, final int i) {
        ThreadListItem view = forumListItemViewWrapper.getView();
        final GroupThread thread = items.get(i);
        view.formatHelper = formatHelper;
        view.mSettingPrefHelper = mSettingPrefHelper;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentActivity_.intent(mActivity).mThreadId(thread.getId()).start();
            }
        });

        view.bind(thread);
    }
}
