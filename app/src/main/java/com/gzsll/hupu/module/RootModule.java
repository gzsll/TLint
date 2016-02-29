package com.gzsll.hupu.module;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.google.gson.Gson;
import com.gzsll.hupu.AppApplication_;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.support.okhttp.CookieInterceptor;
import com.gzsll.hupu.support.okhttp.HttpLoggingInterceptor;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.utils.FileHelper;
import com.gzsll.hupu.ui.activity.AccountActivity_;
import com.gzsll.hupu.ui.activity.BrowserActivity_;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.ui.activity.LoginActivity_;
import com.gzsll.hupu.ui.activity.MainActivity_;
import com.gzsll.hupu.ui.activity.NoticeActivity_;
import com.gzsll.hupu.ui.activity.NotificationActivity_;
import com.gzsll.hupu.ui.activity.PhotoGalleryActivity_;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.ReplyDetailActivity_;
import com.gzsll.hupu.ui.activity.ReportActivity_;
import com.gzsll.hupu.ui.activity.SettingActivity_;
import com.gzsll.hupu.ui.activity.SplashActivity_;
import com.gzsll.hupu.ui.activity.ThreadActivity_;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.ui.fragment.AccountFragment_;
import com.gzsll.hupu.ui.fragment.BoardListFragment_;
import com.gzsll.hupu.ui.fragment.ContentFragment_;
import com.gzsll.hupu.ui.fragment.LoginFragment_;
import com.gzsll.hupu.ui.fragment.MDColorsDialogFragment_;
import com.gzsll.hupu.ui.fragment.MessageAtFragment_;
import com.gzsll.hupu.ui.fragment.MessageReplyFragment_;
import com.gzsll.hupu.ui.fragment.NewsFragment_;
import com.gzsll.hupu.ui.fragment.NewsListFragment_;
import com.gzsll.hupu.ui.fragment.NotificationFragment_;
import com.gzsll.hupu.ui.fragment.PictureItemFragment_;
import com.gzsll.hupu.ui.fragment.SettingFragment_;
import com.gzsll.hupu.ui.fragment.ThreadListFragment_;
import com.gzsll.hupu.ui.fragment.ThreadRecommendFragment_;
import com.gzsll.hupu.ui.fragment.TopicFragment_;
import com.gzsll.hupu.widget.HuPuWebView;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author gzsll
 */

@Module(
        includes = {
                DBModule.class, ApiModule.class, HelperModule.class
        },
        injects = {
                AppApplication_.class,
                MainActivity_.class, ContentActivity_.class, PostActivity_.class, ImagePreviewActivity_.class, ThreadActivity_.class, BrowserActivity_.class,
                PhotoGalleryActivity_.class, ReplyDetailActivity_.class, SettingActivity_.class, NoticeActivity_.class, LoginActivity_.class, AccountActivity_.class,
                SplashActivity_.class, UserProfileActivity_.class, NotificationActivity_.class, NotificationFragment_.class,
                ThreadListFragment_.class, TopicFragment_.class, BoardListFragment_.class, PictureItemFragment_.class, MessageAtFragment_.class, MessageReplyFragment_.class,
                FileHelper.class, MDColorsDialogFragment_.class, SettingFragment_.class, ContentFragment_.class, NewsFragment_.class, NewsListFragment_.class
                , LoginFragment_.class, AccountFragment_.class, HuPuWebView.class, ReportActivity_.class, ThreadRecommendFragment_.class
        },
        library = true
)
public class RootModule {

    private final Context context;

    public RootModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    @Provides
    @Singleton
    public Bus provideBusEvent() {
        return new Bus();
    }


    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(CookieInterceptor mCookieInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(20 * 1000, TimeUnit.MILLISECONDS).readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        builder.addInterceptor(mCookieInterceptor);
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("fresco")
    OkHttpClient provideFrescoOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(20 * 1000, TimeUnit.MILLISECONDS).readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        return builder.build();
    }


    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Singleton
    @Provides
    S3ClientOptions provideS3ClientOptions() {
        S3ClientOptions options = new S3ClientOptions();
        options.setPathStyleAccess(true);
        return options;
    }

    @Singleton
    @Provides
    AmazonS3Client provideAmazonS3Client(S3ClientOptions options) {
        AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials(Constants.BOX_APP_KEY, Constants.BOX_APP_SECRETE));
        s3client.setS3ClientOptions(options);
        s3client.setEndpoint(Constants.BOX_END_POINT);
        return s3client;
    }

    @Singleton
    @Provides
    TransferManager provideTransferManager(AmazonS3Client client) {
        return new TransferManager(client);
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(Context mContext) {
        return (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @Provides
    @Singleton
    CookieInterceptor provideCookieInterceptor(UserStorage mUserStorage) {
        return new CookieInterceptor(mUserStorage);
    }

}
