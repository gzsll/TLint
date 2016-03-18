package com.gzsll.hupu.ui.activity;

import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.gzsll.hupu.R;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.UserDao;
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

    public static final String ACTION_NOTIFICATION_MESSAGE = "com.gzsll.hupu.ACTION_NOTIFICATION_MESSAGE";

    @Inject
    UserStorage mUserStorage;
    @Inject
    UserDao mUserDao;

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
        AlphaAnimation aa = new AlphaAnimation(0.7f, 1.0f);
        aa.setDuration(2000);
        splash.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity.startActivity(SplashActivity.this);
                String action = getIntent().getAction();
                if (TextUtils.equals(action, ACTION_NOTIFICATION_MESSAGE)) {
                    MessageListActivity.startActivity(SplashActivity.this);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
