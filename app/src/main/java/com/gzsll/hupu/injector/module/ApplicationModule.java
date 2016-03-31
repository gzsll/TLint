package com.gzsll.hupu.injector.module;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.components.okhttp.CookieInterceptor;
import com.gzsll.hupu.components.okhttp.HttpLoggingInterceptor;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author gzsll
 */
@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
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

    @Provides
    @Singleton
    UserStorage provideUserStorage(SettingPrefHelper mSettingPrefHelper, Context mContext) {
        return new UserStorage(mSettingPrefHelper, mContext);
    }

}
