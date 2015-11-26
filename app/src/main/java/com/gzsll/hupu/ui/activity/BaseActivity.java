package com.gzsll.hupu.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.support.utils.SystemBarTintManager;
import com.gzsll.hupu.support.utils.ThemeHelper;
import com.squareup.otto.Bus;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

/**
 * @author sll
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    Bus bus;
    @Inject
    UserStorage userStorage;
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    ThemeHelper mThemeHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;

    private int theme = 0;// 当前界面设置的主题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        if (savedInstanceState == null) {
            theme = configTheme();
        } else {
            theme = savedInstanceState.getInt("theme");
        }
        // 设置主题
        if (theme > 0)
            setTheme(theme);
        super.onCreate(savedInstanceState);
        setTranslucentStatus(isApplyStatusBarTranslucency());
        setSystemBarTintEnabled(isApplyKitKatTranslucency());
        bus.register(this);
        AppManager.getAppManager().addActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    protected int configTheme() {
        return mThemeHelper.themeArr[mSettingPrefHelper.getThemeIndex()];
    }

    private void injectDependencies() {
        ((AppApplication) getApplicationContext()).inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public User getUser() {
        return userStorage.getUser();
    }

    public boolean isLogin() {
        return userStorage.isLogin();
    }

    public void logout() {
        userStorage.logout();
    }

    public int getThemeColor() {
        return mResourceHelper.getThemeColor(this);
    }


    public int getStatusBarHeight() {
        return mResourceHelper.getStatusBarHeight(this);
    }


    public void setStatusBarMargin(View view) {
        if (Build.VERSION.SDK_INT < 19 || view == null
                || view.getLayoutParams() == null) {
            return;
        }
        if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            lp.topMargin = lp.topMargin + getStatusBarHeight();
            view.requestLayout();
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            lp.topMargin = lp.topMargin + getStatusBarHeight();
            view.requestLayout();
        } else if (view.getLayoutParams() instanceof DrawerLayout.LayoutParams) {
            DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) view
                    .getLayoutParams();
            lp.topMargin = lp.topMargin + getStatusBarHeight();
            view.requestLayout();
        }

    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    /**
     * is applyStatusBarTranslucency
     *
     * @return
     */
    protected abstract boolean isApplyStatusBarTranslucency();

    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    protected abstract boolean isApplyKitKatTranslucency();

    /**
     * use SystemBarTintManager
     */
    protected void setSystemBarTintEnabled(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            if (on) {
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(getThemeColor());
            } else {
                tintManager.setStatusBarTintEnabled(false);
                tintManager.setStatusBarTintDrawable(null);
            }
        }

    }


}
