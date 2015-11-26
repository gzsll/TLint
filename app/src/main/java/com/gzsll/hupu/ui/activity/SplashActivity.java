package com.gzsll.hupu.ui.activity;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.UserStorage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/11.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Inject
    UserStorage mUserStorage;

    @AfterViews
    void init() {
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
