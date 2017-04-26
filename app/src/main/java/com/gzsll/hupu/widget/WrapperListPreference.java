package com.gzsll.hupu.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by sll on 15/4/27.
 */
public class WrapperListPreference extends ListPreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrapperListPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrapperListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WrapperListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private AlertDialog.Builder mBuilder;

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        mBuilder = builder;
    }

    private String getFieldValue(String filed) throws Exception {
        Field titleField =
                WrapperListPreference.class.getSuperclass().getSuperclass().getDeclaredField(filed);
        titleField.setAccessible(true);
        return titleField.get(this).toString();
    }

    @Override
    protected void showDialog(Bundle state) {
        try {
            if (Build.VERSION.SDK_INT < 100) {
                CharSequence[] mEntries = getEntries();
                String[] mEntriesStrArr = new String[mEntries.length];
                for (int i = 0; i < mEntries.length; i++)
                    mEntriesStrArr[i] = mEntries[i].toString();

                Method method =
                        WrapperListPreference.class.getSuperclass().getDeclaredMethod("getValueIndex");
                method.setAccessible(true);
                int mClickedDialogEntryIndex = Integer.parseInt(method.invoke(this).toString());

                String dialogTitle = getFieldValue("mDialogTitle");
                String mNegativeButtonText = getFieldValue("mNegativeButtonText");

                new AlertDialogWrapper.Builder(getContext()).setTitle(dialogTitle)
                        .setSingleChoiceItems(mEntriesStrArr, mClickedDialogEntryIndex,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Field titleField = WrapperListPreference.class.getSuperclass()
                                                    .getDeclaredField("mClickedDialogEntryIndex");
                                            titleField.setAccessible(true);
                                            titleField.set(WrapperListPreference.this, which);
                                        } catch (Exception e) {
                                        }

                                        WrapperListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                        dialog.dismiss();
                                    }
                                })
                        .setOnDismissListener(WrapperListPreference.this)
                        .setNegativeButton(mNegativeButtonText, WrapperListPreference.this)
                        .show();
            } else {
                super.showDialog(state);
            }
        } catch (Exception e) {
            super.showDialog(state);
        }
    }
}
