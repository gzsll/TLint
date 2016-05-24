package com.gzsll.hupu.ui.splash;

import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.main.MainActivity;
import com.gzsll.hupu.ui.messagelist.MessageActivity;
import com.gzsll.hupu.util.ChannelUtils;
import com.gzsll.hupu.util.UpdateAgent;
import com.umeng.analytics.MobclickAgent;
import javax.inject.Inject;

/**
 * Created by sll on 2016/3/11.
 */
public class SplashActivity extends BaseActivity {

  public static final String ACTION_NOTIFICATION_MESSAGE =
      "com.gzsll.hupu.ACTION_NOTIFICATION_MESSAGE";

  @Bind(R.id.splash) FrameLayout splash;

  @Inject UpdateAgent mUpdateAgent;

  @Override public int initContentView() {
    return R.layout.activity_splash;
  }

  @Override public void initInjector() {
    DaggerSplashComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .splashModule(new SplashModule(this))
        .build()
        .inject(this);
  }

  @Override public void initUiAndListener() {
    ButterKnife.bind(this);
    mUpdateAgent.checkUpdate(false);
    MobclickAgent.UMAnalyticsConfig config =
        new MobclickAgent.UMAnalyticsConfig(this, "55f1993be0f55a0fd9004fbc",
            ChannelUtils.getChannel(this));
    MobclickAgent.startWithConfigure(config);
    AlphaAnimation aa = new AlphaAnimation(0.7f, 1.0f);
    aa.setDuration(2000);
    splash.startAnimation(aa);
    aa.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        MainActivity.startActivity(SplashActivity.this);
        String action = getIntent().getAction();
        if (TextUtils.equals(action, ACTION_NOTIFICATION_MESSAGE)) {
          MessageActivity.startActivity(SplashActivity.this);
        }
        finish();
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
  }

  @Override protected boolean isApplyStatusBarTranslucency() {
    return true;
  }

  @Override protected boolean isApplyStatusBarColor() {
    return false;
  }
}
