package com.gzsll.hupu.helper;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by sll on 2015/11/26.
 */
public class ConfigHelper {
    private SettingPrefHelper mSettingPrefHelper;

    private String cachePath;

    public ConfigHelper(SettingPrefHelper mSettingPrefHelper) {
        this.mSettingPrefHelper = mSettingPrefHelper;
    }


    public String getCachePath() {
        if (!TextUtils.isEmpty(cachePath)) {
            return cachePath;
        }
        cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "gzsll" + File.separator + "cache" + File.separator;
        File cache = new File(cachePath);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cachePath;
    }


    private String uploadPath;

    public String getUploadPath() {
        if (!TextUtils.isEmpty(uploadPath)) {
            return uploadPath;
        }
        uploadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "gzsll" + File.separator + "upload" + File.separator;
        File upload = new File(uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return uploadPath;
    }

    private String picSavePath;

    public String getPicSavePath() {
        if (!TextUtils.isEmpty(picSavePath)) {
            return picSavePath;
        }
        picSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mSettingPrefHelper.getPicSavePath() + File.separator;
        File file = new File(picSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picSavePath;
    }
}
