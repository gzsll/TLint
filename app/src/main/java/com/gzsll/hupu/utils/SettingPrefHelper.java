package com.gzsll.hupu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/5/16.
 */
public class SettingPrefHelper {

    private SharedPreferences sharedPreferences;

    private Context context;

    public SettingPrefHelper(Context context) {
        this.context = context;
    }


    public boolean getOffline() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("SettingPref", Context.MODE_PRIVATE);
        }

        return sharedPreferences.getBoolean("Offline", true);

    }


    public int getThemeIndex() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("SettingPref", Context.MODE_PRIVATE);
        }

        return sharedPreferences.getInt("ThemeIndex", 9);
    }


    public void setOffline(boolean isOffline) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("SettingPref", Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean("Offline", isOffline).apply();

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


    public boolean getLoadPic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pLoadPic", true);
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

}
