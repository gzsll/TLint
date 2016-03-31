package com.gzsll.hupu.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/5/16.
 */
public class SettingPrefHelper {


    private Context context;

    public SettingPrefHelper(Context context) {
        this.context = context;
    }


    public boolean getOffline() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("Offline", true);

    }


    public int getThemeIndex() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("ThemeIndex", 9);
    }

    public void setThemeIndex(int index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt("ThemeIndex", index).apply();
    }


    public void setOffline(boolean isOffline) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.edit().putBoolean("Offline", isOffline).apply();

    }

    public String getPicSavePath() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("PicSavePath", "gzsll");
    }

    public void setPicSavePath(String path) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("PicSavePath", path).apply();
    }


    /**
     * 正文字体大小
     *
     * @return
     */
    public static int[] txtSizeResArr = new int[]{R.dimen.text_size_12, R.dimen.text_size_13, R.dimen.text_size_14, R.dimen.text_size_15,
            R.dimen.text_size_16, R.dimen.text_size_17, R.dimen.text_size_18, R.dimen.text_size_19,
            R.dimen.text_size_20};
    /**
     * 标题字体大小
     */
    public static int[] titleSizeResArr = new int[]{R.dimen.text_size_15,
            R.dimen.text_size_16, R.dimen.text_size_17, R.dimen.text_size_18, R.dimen.text_size_19,
            R.dimen.text_size_20, R.dimen.text_size_21, R.dimen.text_size_22, R.dimen.text_size_23};

    public int getTextSize() {
        return context.getResources().getDimensionPixelSize(txtSizeResArr[getTextSizePref()]);
    }

    public int getTitleSize() {
        return context.getResources().getDimensionPixelSize(titleSizeResArr[getTextSizePref()]);
    }

    private int getTextSizePref() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pTextSize", "3"));
    }

    public String getThreadSort() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pThreadSort", "0")) == 0 ? Constants.THREAD_TYPE_NEW : Constants.THREAD_TYPE_HOT;
    }

    public boolean getNightModel() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNightMode", false);
    }

    public void setNightModel(boolean nightModel) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("pNightMode", nightModel).apply();
    }


    public boolean getLoadPic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pLoadPic", true);
    }

    public boolean getNotification() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNotification", true);
    }

    public boolean getLoadOriginPic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pLoadOriginPic", false);
    }

    public boolean getAutoUpdate() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pAutoUpdate", true);
    }

    public boolean getSingleLine() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pSingleLine", false);
    }

    public int getSwipeBackEdgeMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pSwipeBackEdgeMode", "0"));
    }


    public static int[] offlineCountArr = new int[]{50, 100, 150, 200};

    public int getOfflineCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return offlineCountArr[Integer.parseInt(prefs.getString("pOfflineCount", "0"))];
    }

    public String getLoginUid() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("loginUid", "");

    }


    public void setLoginUid(String uid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("loginUid", uid).apply();

    }

}
