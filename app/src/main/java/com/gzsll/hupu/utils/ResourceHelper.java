package com.gzsll.hupu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gzsll.hupu.R;

/**
 * Created by sll on 2015/9/6 0006.
 */
public class ResourceHelper {


    public int getThemeColor(Context mContext) {
        int materialBlue = mContext.getResources().getColor(R.color.md_green_500);
        return resolveColor(mContext, R.attr.theme_color, materialBlue);
    }

    private int resolveColor(Context mContext, @AttrRes int attr, int fallback) {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void setFabBtnColor(Activity activity, FloatingActionButton fab) {
        // 更新FAB的颜色
        fab.setColorNormal(getThemeColor(activity));
        fab.setColorPressed(getThemeColor(activity));
        fab.setColorRipple(getThemeColor(activity));
    }

    public void setFabMenuColor(Activity activity, FloatingActionMenu fab) {
        // 更新FAB的颜色
        fab.setMenuButtonColorNormal(getThemeColor(activity));
        fab.setMenuButtonColorPressed(getThemeColor(activity));
        fab.setMenuButtonColorRipple(getThemeColor(activity));
    }


}
