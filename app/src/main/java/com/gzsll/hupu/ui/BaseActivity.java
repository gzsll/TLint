package com.gzsll.hupu.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.helper.StatusBarUtil;
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


    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build();
        mActivityComponent.inject(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        setTranslucentStatus(isApplyStatusBarTranslucency());
        setStatusBarColor(isApplyStatusBarColor());
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


    private void initTheme() {
        if (mSettingPrefHelper.getNightModel()) {
            int theme;

            try {
                theme = getPackageManager().getActivityInfo(getComponentName(), 0).theme;
            } catch (PackageManager.NameNotFoundException e) {
                return;
            }
            if (theme == R.style.AppThemeLight) {
                theme = R.style.AppThemeDark;
            } else if (theme == R.style.AppThemeLight_FitsStatusBar) {
                theme = R.style.AppThemeDark_FitsStatusBar;
            } else if (theme == R.style.AppThemeLight_NoTranslucent) {
                theme = R.style.AppThemeDark_NoTranslucent;
            }
            setTheme(theme);
        }
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

    protected abstract boolean isApplyStatusBarColor();

    /**
     * use SystemBarTintManager
     */
    public void setStatusBarColor(boolean on) {
        if (on) {
            StatusBarUtil.setColor(this, mResourceHelper.getThemeColor(this), 0);
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
