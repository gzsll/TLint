package com.gzsll.hupu.support.utils;

/*  * 文 件 名:  DataCleanManager.java  * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录  */

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 本应用数据清除管理器
 */
public class DataCleanHelper {

    private Context context;

    public DataCleanHelper(Context context) {
        this.context = context;
    }


    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public void cleanInternalCache() {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context
     */
    public void cleanDatabases() {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public void cleanSharedPreference() {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库 * * @param context * @param dbName
     */
    public void cleanDatabaseByName(String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context
     */
    public void cleanFiles() {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public void cleanExternalCache() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath
     */
    public void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    public void cleanApplicationData() {
        cleanInternalCache();
        cleanExternalCache();
        cleanDatabases();
        cleanSharedPreference();
        cleanFiles();
    }

    public void cleadAPplicationCache() {
        cleanInternalCache();
        cleanExternalCache();
        cleanFiles();
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private void deleteFilesByDirectory(File directory) {
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