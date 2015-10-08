package com.gzsll.hupu.ui.activity;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by sll on 2015/9/13.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends BaseSwipeBackActivity {


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }
}
