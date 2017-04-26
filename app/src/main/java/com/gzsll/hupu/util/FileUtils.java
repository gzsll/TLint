package com.gzsll.hupu.util;

import android.os.Environment;

import java.io.IOException;

/**
 * Created by sll on 2015/3/7.
 */
public class FileUtils {

    public static boolean hasSDCard() {
        boolean mHasSDcard = false;
        if (Environment.MEDIA_MOUNTED.endsWith(Environment.getExternalStorageState())) {
            mHasSDcard = true;
        } else {
            mHasSDcard = false;
        }

        return mHasSDcard;
    }

    public static String getSdcardPath() {

        if (hasSDCard()) return Environment.getExternalStorageDirectory().getAbsolutePath();

        return "/sdcard/";
    }

    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


