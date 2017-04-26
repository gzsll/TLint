package com.gzsll.hupu.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by sll on 2015/5/16.
 */
public class CacheUtil {

    public static String getCacheSize(Context context) {
        // 计算缓存大小
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = context.getFilesDir();
        File cacheDir = context.getCacheDir();
        File ExternalCacheDir = context.getExternalCacheDir();

        fileSize += FormatUtil.getDirSize(filesDir);
        fileSize += FormatUtil.getDirSize(cacheDir);
        fileSize += FormatUtil.getDirSize(ExternalCacheDir);
        if (fileSize > 0) cacheSize = FormatUtil.formatFileSize(fileSize);
        return cacheSize;
    }

    public static void cleanApplicationCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanFiles(context);
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                if (item.isDirectory()) {
                    deleteFilesByDirectory(item);
                }
                item.delete();
            }
            directory.delete();
        }
    }
}
