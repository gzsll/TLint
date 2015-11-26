package com.gzsll.hupu.module;

import android.content.Context;

import com.gzsll.hupu.support.utils.CacheHelper;
import com.gzsll.hupu.support.utils.ConfigHelper;
import com.gzsll.hupu.support.utils.DataCleanHelper;
import com.gzsll.hupu.support.utils.FileHelper;
import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.HtmlHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.support.utils.SecurityHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.support.utils.ThemeHelper;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2015/3/7.
 */
@Module(
        complete = false,
        library = true
)
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
    RequestHelper provideRequestUtil(SecurityHelper securityHelper, Context context) {
        return new RequestHelper(securityHelper, context);
    }

    @Provides
    @Singleton
    OkHttpHelper provideOkHttpHelper(OkHttpClient okHttpClient) {
        return new OkHttpHelper(okHttpClient);
    }


    @Provides
    @Singleton
    CacheHelper provideCacheHelper(Context context, FormatHelper mFormatHelper) {
        return new CacheHelper(context, mFormatHelper);
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
    HtmlHelper provideHtmlHelper(ConfigHelper mConfigHelper, FileHelper mFileHelper, OkHttpHelper mOkHttpHelper) {
        return new HtmlHelper(mConfigHelper, mFileHelper, mOkHttpHelper);
    }


}
