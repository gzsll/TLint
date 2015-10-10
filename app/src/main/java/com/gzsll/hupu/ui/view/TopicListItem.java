package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.bean.Topic;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/5/28.
 */
@EViewGroup(R.layout.item_list_topic)
public class TopicListItem extends LinearLayout {
    public TopicListItem(Context context) {
        super(context);
    }

    public TopicListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById
    TextView tvTitle, tvBoard, tvReply;

    public void bind(Topic topic) {
        if (topic != null) {
            tvTitle.setText(topic.getTitle());
            tvBoard.setText(topic.getGroupName());
            tvReply.setText("回复:" + topic.getReplies());
        }
    }

}
