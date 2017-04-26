package com.gzsll.hupu.ui.thread.list;

import android.content.Context;
import android.content.Intent;

import com.gzsll.hupu.R;
import com.gzsll.hupu.injector.HasComponent;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;

import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class ThreadListActivity extends BaseSwipeBackActivity
        implements HasComponent<ThreadListComponent> {

    public static void startActivity(Context mContext, String fid) {
        Intent intent = new Intent(mContext, ThreadListActivity.class);
        intent.putExtra("fid", fid);
        mContext.startActivity(intent);
    }

    private ThreadListComponent mThreadListComponent;

    @Override
    public int initContentView() {
        return R.layout.base_content_empty;
    }

    @Override
    public void initInjector() {
        String fid = getIntent().getStringExtra("fid");
        mThreadListComponent = DaggerThreadListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .threadListModule(new ThreadListModule(fid))
                .build();
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new ThreadListFragment())
                .commit();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    public ThreadListComponent getComponent() {
        return mThreadListComponent;
    }
}
