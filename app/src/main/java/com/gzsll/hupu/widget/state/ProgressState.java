package com.gzsll.hupu.widget.state;

import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/3/13.
 */
public class ProgressState extends AbstractShowState {

    @Override
    public void show(boolean animate) {
        showViewById(R.id.epf_progress, animate);
    }

    @Override
    public void dismiss(boolean animate) {
        dismissViewById(R.id.epf_progress, animate);
    }
}
