package com.gzsll.hupu.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.AccountFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/11/23.
 */
@EActivity(R.layout.base_content_toolbar_layout)
public class AccountActivity extends BaseSwipeBackActivity {
    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        initToolBar(toolbar);
        setTitle("账号管理");
        getSupportFragmentManager().beginTransaction().replace(R.id.content,AccountFragment_.builder().build()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            LoginActivity_.intent(this).start();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
