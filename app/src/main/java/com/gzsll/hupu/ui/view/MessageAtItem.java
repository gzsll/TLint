package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.storage.bean.MessageAt;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/9/8.
 */
@EViewGroup(R.layout.item_list_message_at)
public class MessageAtItem extends LinearLayout {
    public MessageAtItem(Context context) {
        super(context);
    }

    public MessageAtItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvName, tvTime;


    public void init(MessageAt messageAt) {
        ivIcon.setImageURI(Uri.parse(messageAt.getUserInfo().getIcon()));
        tvName.setText(messageAt.getUserInfo().getUsername());
        tvTime.setText(messageAt.getFormatTime());
    }

}
