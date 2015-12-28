package com.gzsll.hupu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.ChannelUtil;
import com.umeng.analytics.AnalyticsConfig;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/11.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Inject
    UserStorage mUserStorage;
    @ViewById
    FrameLayout splash;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsConfig.setAppkey(this, "55f1993be0f55a0fd9004fbc");
        AnalyticsConfig.setChannel(ChannelUtil.getChannel(this));
        if (mUserStorage.isLogin()) {
            toMain();
        } else {
            LoginActivity_.intent(this).start();
            finish();
        }
    }


    @UiThread(delay = 2000)
    void toMain() {
        MainActivity_.intent(this).start();
        finish();
    }


    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected int configTheme() {
        return R.style.AppTheme_NoneTranslucent;
    }
}
