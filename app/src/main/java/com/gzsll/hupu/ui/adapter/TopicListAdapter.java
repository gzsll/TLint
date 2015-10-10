package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.gzsll.hupu.support.storage.bean.Topic;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.view.TopicListItem;
import com.gzsll.hupu.ui.view.TopicListItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/5/28.
 */
public class TopicListAdapter extends BaseListAdapter<Topic, TopicListItem> {

    @Inject
    public TopicListAdapter() {
    }


    @Override
    protected TopicListItem onCreateItemView(Context context) {
        return TopicListItem_.build(context);
    }

    @Override
    protected void onBindView(TopicListItem view, int position, final Topic data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentActivity_.intent(mActivity).mThreadId(data.getId()).start();
            }
        });
        view.bind(data);
    }
}
