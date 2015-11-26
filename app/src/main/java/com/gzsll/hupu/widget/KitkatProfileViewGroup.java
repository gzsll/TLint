package com.gzsll.hupu.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gzsll.hupu.support.utils.SystemBarUtils;

/**
 * Created by wangdan on 15/4/26.
 */
public class KitkatProfileViewGroup extends FrameLayout {

    public KitkatProfileViewGroup(Context context) {
        super(context);

        setInit();
    }

    public KitkatProfileViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInit();
    }

    public KitkatProfileViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setInit();
    }

    private void setInit() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setPadding(getPaddingLeft(),
                    getPaddingTop(),
                    getPaddingRight(),
                    getPaddingBottom() + SystemBarUtils.getNavigationBarHeight(getContext()));

//            setBackgroundColor(Utils.resolveColor(getContext(), R.attr.colorPrimary, Color.BLACK));
        }
    }

}
