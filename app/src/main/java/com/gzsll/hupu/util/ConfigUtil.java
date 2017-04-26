package com.gzsll.hupu.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by sll on 2015/11/26.
 */
public class ConfigUtil {

    private static String cachePath;

    public static String getCachePath() {
        if (!TextUtils.isEmpty(cachePath)) {
            return cachePath;
        }
        cachePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "gzsll"
                + File.separator
                + "cache"
                + File.separator;
        File cache = new File(cachePath);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cachePath;
    }

    private static String uploadPath;

    public static String getUploadPath() {
        if (!TextUtils.isEmpty(uploadPath)) {
            return uploadPath;
        }
        uploadPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "gzsll"
                + File.separator
                + "upload"
                + File.separator;
        File upload = new File(uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return uploadPath;
    }

    private static String picSavePath;

    public static String getPicSavePath(Context context) {
        if (!TextUtils.isEmpty(picSavePath)) {
            return picSavePath;
        }
        picSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + SettingPrefUtil.getPicSavePath(context)
                + File.separator;
        File file = new File(picSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picSavePath;
    }
}
