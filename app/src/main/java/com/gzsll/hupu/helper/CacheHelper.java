package com.gzsll.hupu.helper;

import android.content.Context;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by sll on 2015/5/16.
 */
public class CacheHelper {
    Logger logger = Logger.getLogger(CacheHelper.class.getSimpleName());

    private Context mContext;
    private FormatHelper mFormatHelper;

    public CacheHelper(Context context, FormatHelper mFormatHelper) {
        this.mContext = context;
        this.mFormatHelper = mFormatHelper;
    }


    public String getCacheSize() {
        // 计算缓存大小
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = mContext.getFilesDir();
        File cacheDir = mContext.getCacheDir();
        File ExternalCacheDir = mContext.getExternalCacheDir();

        fileSize += mFormatHelper.getDirSize(filesDir);
        fileSize += mFormatHelper.getDirSize(cacheDir);
        fileSize += mFormatHelper.getDirSize(ExternalCacheDir);
        if (fileSize > 0)
            cacheSize = mFormatHelper.formatFileSize(fileSize);
        return cacheSize;
    }


}
