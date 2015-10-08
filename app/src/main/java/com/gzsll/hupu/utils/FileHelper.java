package com.gzsll.hupu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sll on 2015/3/7.
 */
public class FileHelper {

    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    public boolean hasSDCard() {
        boolean mHasSDcard = false;
        if (Environment.MEDIA_MOUNTED.endsWith(Environment.getExternalStorageState())) {
            mHasSDcard = true;
        } else {
            mHasSDcard = false;
        }

        return mHasSDcard;
    }

    public String getSdcardPath() {

        if (hasSDCard())
            return Environment.getExternalStorageDirectory().getAbsolutePath();

        return "/sdcard/";
    }


    public String stringFromAssetsFile(String fileName) {
        AssetManager manager = context.getAssets();
        InputStream file;
        try {
            file = manager.open(fileName);
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String length(long length) {
        String str = "0B";
        if (length == 0) {
            return str;
        }
        if (length < 1048576) {
            return Math.round(((double) length) / 1024) + "KBbyte";
        }
        return Math.round(((double) length) / 1048576) + "MBbyte";
    }

    public boolean copy(File oldFile, File newFile) {
        if (!oldFile.exists()) {
            return false;
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(oldFile);
            outputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
