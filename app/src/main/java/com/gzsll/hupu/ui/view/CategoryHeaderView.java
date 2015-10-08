package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.category_header)
public class CategoryHeaderView extends LinearLayout {
    public CategoryHeaderView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public CategoryHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @ViewById(R.id.title)
    TextView title;

    public void setTitleText(String text) {
        title.setText(text);
    }
}
