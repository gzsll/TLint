package com.gzsll.hupu.ui.activity;

import android.os.Handler;
import android.widget.FrameLayout;

import com.gzsll.hupu.R;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.ChannelHelper;
import com.gzsll.hupu.ui.BaseActivity;
import com.umeng.analytics.AnalyticsConfig;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class SplashActivity extends BaseActivity {

    @Inject
    UserStorage mUserStorage;
    @Bind(R.id.splash)
    FrameLayout splash;


    @Override
    public int initContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        AnalyticsConfig.setAppkey(this, "55f1993be0f55a0fd9004fbc");
        AnalyticsConfig.setChannel(ChannelHelper.getChannel(this));
        if (mUserStorage.isLogin()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.startActivity(SplashActivity.this);
                    finish();
                }
            }, 2000);
        } else {
            LoginActivity.startAcitivity(this);
            finish();
        }
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    protected int configTheme() {
        return R.style.AppTheme_NoneTranslucent;
    }
}
