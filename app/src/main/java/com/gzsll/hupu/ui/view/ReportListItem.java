package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/12/12.
 */
@EViewGroup(R.layout.item_list_report)
public class ReportListItem extends RelativeLayout {

    @ViewById
    TextView tvType;
    @ViewById
    ImageView ivCheck;


    public ReportListItem(Context context) {
        super(context);
    }

    public ReportListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIvCheckVisibility(int visibility) {
        ivCheck.setVisibility(visibility);
    }

    public void setText(String text) {
        tvType.setText(text);
    }


}
