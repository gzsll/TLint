package com.gzsll.hupu.injector.module;

import android.content.Context;

import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.CacheHelper;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.DataCleanHelper;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.FormatHelper;
import com.gzsll.hupu.helper.NetWorkHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.helper.SecurityHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.helper.ShareHelper;
import com.gzsll.hupu.helper.StringHelper;
import com.gzsll.hupu.helper.ThemeHelper;
import com.gzsll.hupu.helper.ToastHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by sll on 2015/3/7.
 */
@Module
public class HelperModule {

    @Provides
    @Singleton
    FormatHelper provideFormatHelper() {
        return new FormatHelper();
    }

    @Provides
    @Singleton
    FileHelper provideFileHelper(Context context) {
        return new FileHelper(context);
    }

    @Provides
    @Singleton
    SecurityHelper provideSecurityHelper() {
        return new SecurityHelper();
    }


    @Provides
    @Singleton
    NetWorkHelper provideNetWorkHelper(Context mContext) {
        return new NetWorkHelper(mContext);
    }


    @Provides
    @Singleton
    RequestHelper provideRequestUtil(SecurityHelper securityHelper, Context context, UserStorage mUserStorage) {
        return new RequestHelper(securityHelper, context, mUserStorage);
    }

    @Provides
    @Singleton
    OkHttpHelper provideOkHttpHelper(OkHttpClient okHttpClient) {
        return new OkHttpHelper(okHttpClient);
    }


    @Provides
    @Singleton
    SettingPrefHelper provideSettingPrefHelper(Context context) {
        return new SettingPrefHelper(context);
    }


    @Provides
    @Singleton
    ResourceHelper provideResourceHelper() {
        return new ResourceHelper();
    }

    @Provides
    @Singleton
    ThemeHelper provideThemeHelper(Context mContext, SettingPrefHelper mSettingPrefHelper) {
        return new ThemeHelper(mContext, mSettingPrefHelper);
    }

    @Provides
    @Singleton
    DataCleanHelper provideDataCleanHelper(Context context) {
        return new DataCleanHelper(context);
    }

    @Provides
    @Singleton
    ConfigHelper provideConfigHelper(SettingPrefHelper mSettingPrefHelper) {
        return new ConfigHelper(mSettingPrefHelper);
    }

    @Provides
    @Singleton
    ToastHelper provideToastHelper(Context mContext) {
        return new ToastHelper(mContext);
    }

    @Provides
    @Singleton
    CacheHelper provideCacheHelper(Context mContext, FormatHelper mFormatHelper) {
        return new CacheHelper(mContext, mFormatHelper);
    }

    @Provides
    @Singleton
    ShareHelper provideShareHelper(Context mContext) {
        return new ShareHelper(mContext);
    }

    @Provides
    @Singleton
    StringHelper provideStringHelper(Context mContext, ToastHelper mToastHelper) {
        return new StringHelper(mContext, mToastHelper);
    }

}
