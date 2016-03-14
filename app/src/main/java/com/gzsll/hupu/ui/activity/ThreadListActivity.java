package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.ThreadListFragment;

import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class ThreadListActivity extends BaseSwipeBackActivity {

    public static void startActivity(Context mContext, String fid) {
        Intent intent = new Intent(mContext, ThreadListActivity.class);
        intent.putExtra("fid", fid);
        mContext.startActivity(intent);
    }

    @Override
    public int initContentView() {
        return R.layout.base_content_empty;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        String fid = getIntent().getStringExtra("fid");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ThreadListFragment.newInstance(fid)).commit();
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
