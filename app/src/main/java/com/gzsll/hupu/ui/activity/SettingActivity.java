package com.gzsll.hupu.ui.activity;

import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.SettingFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/9/7 0007.
 */
@EActivity(R.layout.base_content_toolbar_layout)
public class SettingActivity extends BaseSwipeBackActivity {

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        initToolBar(toolbar);
        setTitle("设置");
        getFragmentManager().beginTransaction().replace(R.id.content, SettingFragment_.builder().build()).commit();
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

}
