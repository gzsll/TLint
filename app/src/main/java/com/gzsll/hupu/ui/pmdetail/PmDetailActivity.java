package com.gzsll.hupu.ui.pmdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/5/6.
 */
public class PmDetailActivity extends BaseSwipeBackActivity {
    public static void startActivity(Context mContext, String uid, String name) {
        Intent intent = new Intent(mContext, PmDetailActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
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
        String name = getIntent().getStringExtra("name");
        String uid = getIntent().getStringExtra("uid");
        setTitle(name);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, PmDetailFragment.newInstance(uid)).commit();
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
