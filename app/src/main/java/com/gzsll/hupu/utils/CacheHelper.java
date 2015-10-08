package com.gzsll.hupu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    private DiskLruCache mDiskLruCache = null;

    public DiskLruCache getCache() {
        try {
            if (mDiskLruCache == null) {
                File cacheDir = getDiskCacheDir();
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                logger.debug(cacheDir.getPath());
                mDiskLruCache = DiskLruCache
                        .open(cacheDir, getAppVersion(mContext), 1, 100 * 1024 * 1024);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    public File getDiskCacheDir() {
        String cachePath;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = mContext.getExternalCacheDir().getPath();
            } else {
                cachePath = mContext.getCacheDir().getPath();
            }
        } catch (Exception e) {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + "cache");
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public void fluchCache(DiskLruCache mDiskLruCache) {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Bitmap getBitmap(DiskLruCache mDiskLruCache, String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            FileDescriptor fileDescriptor = null;
            if (snapShot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            Bitmap bitmap = null;
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getBytes(DiskLruCache mDiskLruCache, String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream in = snapShot.getInputStream(0);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy(in, output);
                return output.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
