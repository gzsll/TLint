package com.gzsll.hupu.ui.activity;

import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.NotificationFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/12/12.
 */
@EActivity(R.layout.base_content_toolbar_layout)
public class NotificationActivity extends BaseSwipeBackActivity {

    @ViewById
    Toolbar toolbar;


    @AfterViews
    void init() {
        initToolBar(toolbar);
        setTitle("论坛消息");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, NotificationFragment_.builder().build()).commit();
    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }
}
