package com.gzsll.hupu.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.helper.SystemBarTintManager;
import com.gzsll.hupu.helper.ThemeHelper;
import com.gzsll.hupu.injector.component.ActivityComponent;
import com.gzsll.hupu.injector.component.DaggerActivityComponent;
import com.gzsll.hupu.injector.module.ActivityModule;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

/**
 * Created by sll on 2016/3/9.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityComponent mActivityComponent;
    private int theme = 0;// 当前界面设置的主题

    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    ThemeHelper mThemeHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build();
        mActivityComponent.inject(this);
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
        setContentView(initContentView());
        initInjector();
        initUiAndListener();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected int configTheme() {
        return mThemeHelper.themeArr[mSettingPrefHelper.getThemeIndex()];
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }


    /**
     * 设置view
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * init UI && Listener
     */
    public abstract void initUiAndListener();


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
                tintManager.setStatusBarTintColor(mResourceHelper.getThemeColor(this));
            } else {
                tintManager.setStatusBarTintEnabled(false);
                tintManager.setStatusBarTintDrawable(null);
            }
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


    public int getStatusBarHeight() {
        return mResourceHelper.getStatusBarHeight(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }


}
