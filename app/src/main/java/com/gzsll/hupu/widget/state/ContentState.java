package com.gzsll.hupu.widget.state;

import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/3/13.
 */
public class ContentState extends AbstractShowState implements ShowState {

    @Override
    public void show(boolean animate) {
        showViewById(R.id.epf_content, animate);
    }

    @Override
    public void dismiss(boolean animate) {
        dismissViewById(R.id.epf_content, animate);
    }
}
