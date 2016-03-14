package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.ContentFragment;

import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentActivity extends BaseSwipeBackActivity {

    public static void startActivity(Context mContext, String fid, String tid, String pid, int page, String title) {
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        intent.putExtra("page", page);
        intent.putExtra("title", title);
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
        String tid = getIntent().getStringExtra("tid");
        int page = getIntent().getIntExtra("page", 1);
        String pid = getIntent().getStringExtra("pid");
        String title = getIntent().getStringExtra("title");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ContentFragment.newInstance(fid, tid, pid, page, title)).commit();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }
}
