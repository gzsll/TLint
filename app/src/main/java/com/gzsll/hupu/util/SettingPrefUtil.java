package com.gzsll.hupu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/5/16.
 */
public class SettingPrefUtil {

    public static boolean getOffline(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("Offline", true);
    }

    public static int getThemeIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("ThemeIndex", 9);
    }

    public static void setThemeIndex(Context context, int index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt("ThemeIndex", index).apply();
    }

    public static void setOffline(Context context, boolean isOffline) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.edit().putBoolean("Offline", isOffline).apply();
    }

    public static String getPicSavePath(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("PicSavePath", "gzsll");
    }

    public static void setPicSavePath(Context context, String path) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("PicSavePath", path).apply();
    }

    /**
     * 正文字体大小
     *
     * @return
     */
    public static int[] txtSizeResArr = new int[]{
            R.dimen.text_size_12, R.dimen.text_size_13, R.dimen.text_size_14, R.dimen.text_size_15,
            R.dimen.text_size_16, R.dimen.text_size_17, R.dimen.text_size_18, R.dimen.text_size_19,
            R.dimen.text_size_20
    };
    /**
     * 标题字体大小
     */
    public static int[] titleSizeResArr = new int[]{
            R.dimen.text_size_15, R.dimen.text_size_16, R.dimen.text_size_17, R.dimen.text_size_18,
            R.dimen.text_size_19, R.dimen.text_size_20, R.dimen.text_size_21, R.dimen.text_size_22,
            R.dimen.text_size_23
    };

    public static int getTextSize(Context context) {
        return context.getResources().getDimensionPixelSize(txtSizeResArr[getTextSizePref(context)]);
    }

    public static int getTitleSize(Context context) {
        return context.getResources().getDimensionPixelSize(titleSizeResArr[getTextSizePref(context)]);
    }

    private static int getTextSizePref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pTextSize", "3"));
    }

    public static String getThreadSort(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pThreadSort", "0")) == 0 ? Constants.THREAD_TYPE_NEW
                : Constants.THREAD_TYPE_HOT;
    }

    public static boolean getNightModel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNightMode", false);
    }

    public static void setNightModel(Context context, boolean nightModel) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("pNightMode", nightModel).apply();
    }

    public static boolean getLoadPic(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pLoadPic", true);
    }

    public static boolean getNotification(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNotification", true);
    }

    public static boolean getLoadOriginPic(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pLoadOriginPic", false);
    }

    public static boolean getAutoUpdate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pAutoUpdate", true);
    }

    public static boolean getSingleLine(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pSingleLine", false);
    }

    public static int getSwipeBackEdgeMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pSwipeBackEdgeMode", "0"));
    }

    public static int[] offlineCountArr = new int[]{50, 100, 150, 200};

    public static int getOfflineCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return offlineCountArr[Integer.parseInt(prefs.getString("pOfflineCount", "0"))];
    }

    public static String getLoginUid(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("loginUid", "");
    }

    public static void setLoginUid(Context context, String uid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("loginUid", uid).apply();
    }

    public static void setNeedExam(Context context, boolean needExam) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("needExam", needExam).apply();
    }

    public static boolean isNeedExam(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("needExam", false);
    }

    public static String getHuPuSign(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("hupuSign", "HUPU_SALT_AKJfoiwer394Jeiow4u309");
    }

    public static void setHuPuSign(Context context, String hupuSign) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("hupuSign", hupuSign).apply();
    }
}
