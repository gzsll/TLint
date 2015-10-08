package com.gzsll.hupu.ui.activity;

import android.os.Bundle;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.ThreadListFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.apache.log4j.Logger;

/**
 * Created by sll on 2015/6/17.
 */
@EActivity
public class ThreadActivity extends BaseSwipeBackActivity {
    Logger logger = Logger.getLogger(ThreadActivity.class.getSimpleName());

    @Extra
    long mGroupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_content_empty);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ThreadListFragment_.builder().mGroupId(mGroupId).build()).commit();
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
