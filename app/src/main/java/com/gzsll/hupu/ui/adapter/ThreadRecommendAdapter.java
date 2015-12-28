package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.view.ThreadListItem;
import com.gzsll.hupu.ui.view.ThreadListItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
public class ThreadRecommendAdapter extends BaseListAdapter<Thread, ThreadListItem> {
    @Inject
    FormatHelper formatHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;

    @Override
    protected ThreadListItem onCreateItemView(Context context) {
        return ThreadListItem_.build(context);
    }

    @Override
    protected void onBindView(ThreadListItem view, int position, final Thread data) {
        view.formatHelper = formatHelper;
        view.mSettingPrefHelper = mSettingPrefHelper;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentActivity_.intent(mActivity).tid(data.tid).fid(data.fid).start();
            }
        });
        view.bind(data);
    }
}
