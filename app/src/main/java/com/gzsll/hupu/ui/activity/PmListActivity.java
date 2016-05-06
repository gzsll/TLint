package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.PmListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/5/6.
 */
public class PmListActivity extends BaseSwipeBackActivity {
    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, PmListActivity.class);
        mContext.startActivity(intent);
    }


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.base_content_toolbar_layout;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        initToolBar(toolbar);
        setTitle("我的短消息");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new PmListFragment()).commit();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }
}
