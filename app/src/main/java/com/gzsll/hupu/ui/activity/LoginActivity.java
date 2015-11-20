package com.gzsll.hupu.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.AccountFragment_;
import com.gzsll.hupu.ui.fragment.LoginFragment;
import com.gzsll.hupu.ui.fragment.LoginFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sll on 2015/9/11.
 */
@EActivity(R.layout.base_content_toolbar_layout)
public class LoginActivity extends BaseSwipeBackActivity {

    @Extra
    boolean login;

    @ViewById
    Toolbar toolbar;


    private Fragment mFragment;

    @AfterViews
    void init() {
        initToolBar(toolbar);
        setTitle(login ? "登录" : "账号管理");
        if (mFragment == null) {
            if (login) {
                mFragment = LoginFragment_.builder().build();
            } else {
                mFragment = AccountFragment_.builder().build();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();

    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.login) {
            ((LoginFragment) mFragment). doLogin();
        }

        return super.onOptionsItemSelected(item);
    }

}
