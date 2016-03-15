package com.gzsll.hupu.injector.component;

import android.app.Activity;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.module.ActivityModule;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.activity.AccountActivity;
import com.gzsll.hupu.ui.activity.BrowserActivity;
import com.gzsll.hupu.ui.activity.ContentActivity;
import com.gzsll.hupu.ui.activity.GalleryActivity;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity;
import com.gzsll.hupu.ui.activity.LoginActivity;
import com.gzsll.hupu.ui.activity.MainActivity;
import com.gzsll.hupu.ui.activity.PostActivity;
import com.gzsll.hupu.ui.activity.ReportActivity;
import com.gzsll.hupu.ui.activity.SplashActivity;
import com.gzsll.hupu.ui.activity.UserProfileActivity;

import dagger.Component;

/**
 * Created by sll on 2016/3/8.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivityContext();

    void inject(BaseActivity mBaseActivity);

    void inject(MainActivity mMainActivity);

    void inject(LoginActivity mLoginActivity);

    void inject(AccountActivity mAccountActivity);

    //
    void inject(BrowserActivity mBrowserActivity);

    //
    void inject(ImagePreviewActivity mImagePreviewActivity);

    //
    void inject(GalleryActivity mGalleryActivity);

    //
    void inject(PostActivity mPostActivity);

    void inject(UserProfileActivity mUserProfileActivity);

    //
    void inject(ReportActivity mReportActivity);

    //
    void inject(SplashActivity mSplashActivity);

    void inject(ContentActivity mActivity);


}
