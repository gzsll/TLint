package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.bean.News;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/10/10.
 */
@EViewGroup(R.layout.item_list_news)
public class NewsListItem extends RelativeLayout {
    public NewsListItem(Context context) {
        super(context);
    }

    public NewsListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvTitle, tvSummary, tvReply;

    public void init(News news) {
        ivIcon.setImageURI(Uri.parse(news.getImg()));
        tvTitle.setText(news.getTitle());
        tvSummary.setText(news.getSummary());
        tvReply.setText(news.getReplies());

    }
}
