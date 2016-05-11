package com.gzsll.hupu.injector.component;

import android.app.Activity;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.module.ActivityModule;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.account.AccountActivity;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.content.ContentActivity;
import com.gzsll.hupu.ui.gallery.GalleryActivity;
import com.gzsll.hupu.ui.imagepreview.ImagePreviewActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.main.MainActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.report.ReportActivity;
import com.gzsll.hupu.ui.splash.SplashActivity;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;

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
