package com.gzsll.hupu.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.pref.SettingPref_;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.utils.CacheHelper;
import com.gzsll.hupu.utils.DataCleanHelper;
import com.gzsll.hupu.utils.FileHelper;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8.
 */
@EFragment
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    @Pref
    SettingPref_ mSettingPref;

    private Preference pTheme;// 主题设置
    private ListPreference pTextSize;// 字体大小
    private Preference pPicSavePath;// 图片保存路径
    private Preference pClearCache;
    private ListPreference pThreadSort;
    private ListPreference pSwipeBackEdgeMode;// 手势返回方向


    @Inject
    CacheHelper mCacheHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppApplication) getActivity().getApplicationContext()).inject(this);
        addPreferencesFromResource(R.xml.setting);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        pTheme = findPreference("pTheme");
        pTheme.setOnPreferenceClickListener(this);
        pTheme.setSummary(getResources().getStringArray(R.array.mdColorNames)[mSettingPref.ThemeIndex().get()]);

        pTextSize = (ListPreference) findPreference("pTextSize");
        pTextSize.setOnPreferenceChangeListener(this);
        setListSetting(Integer.parseInt(prefs.getString("pTextSize", "4")), R.array.txtSizeNum, pTextSize);


        pPicSavePath = findPreference("pPicSavePath");
        pPicSavePath.setOnPreferenceClickListener(this);
        pPicSavePath.setSummary("/sdcard" + File.separator + mSettingPref.PicSavePath().get() + File.separator);


        pClearCache = findPreference("pClearCache");
        pClearCache.setOnPreferenceClickListener(this);
        pClearCache.setSummary(mCacheHelper.getCacheSize());

        pThreadSort = (ListPreference) findPreference("pThreadSort");
        pThreadSort.setOnPreferenceChangeListener(this);
        setListSetting(Integer.parseInt(prefs.getString("pThreadSort", "0")), R.array.sortType, pThreadSort);

        pSwipeBackEdgeMode = (ListPreference) findPreference("pSwipeBackEdgeMode");
        pSwipeBackEdgeMode.setOnPreferenceChangeListener(this);
        setListSetting(Integer.parseInt(prefs.getString("pSwipeBackEdgeMode", "0")), R.array.swipeBackEdgeMode, pSwipeBackEdgeMode);


    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if ("pTextSize".equals(preference.getKey())) {
            setListSetting(Integer.parseInt(newValue.toString()), R.array.txtSizeNum, pTextSize);
        } else if ("pThreadSort".equals(preference.getKey())) {
            setListSetting(Integer.parseInt(newValue.toString()), R.array.sortType, pThreadSort);
        } else if ("pSwipeBackEdgeMode".equals(preference.getKey())) {
            setListSetting(Integer.parseInt(newValue.toString()), R.array.swipeBackEdgeMode, pSwipeBackEdgeMode);
            ((BaseActivity) getActivity()).reload();
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pTheme".equals(preference.getKey())) {
            MDColorsDialogFragment.launch(getActivity());
        } else if ("pPicSavePath".equals(preference.getKey())) {
            modifyImageSavePath();
        } else if ("pClearCache".equals(preference.getKey())) {
            cleanCache();
        }


        return true;
    }

    @Inject
    DataCleanHelper mDataCleanHelper;

    private void cleanCache() {
//        new MaterialDialog.Builder(getActivity()).title("提示").content("正在清空缓存...").progress(true,0).show();
        mDataCleanHelper.cleadAPplicationCache();
        Toast.makeText(getActivity(), "缓存清理成功", Toast.LENGTH_SHORT);
        pClearCache.setSummary(mCacheHelper.getCacheSize());
    }

    private void setTextSize(int value) {
        String[] valueTitleArr = getResources().getStringArray(R.array.txtSizeNum);
        pTextSize.setSummary(valueTitleArr[value]);
    }


    protected void setListSetting(int value, int hintId, ListPreference listPreference) {
        String[] valueTitleArr = getResources().getStringArray(hintId);
        listPreference.setSummary(valueTitleArr[value]);
    }

    @Inject
    FileHelper mFileHelper;


    private void modifyImageSavePath() {
        new MaterialDialog.Builder(getActivity()).title("修改图片保存路径").input(null, mSettingPref.PicSavePath().get(), new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                if (TextUtils.isEmpty(charSequence)) {
                    showToast("路径不能为空");
                    return;
                }
                String path = mFileHelper.getSdcardPath() + File.separator + charSequence + File.separator;
                File file = new File(path);
                if (file.exists() || file.mkdirs()) {
                    mSettingPref.PicSavePath().put(charSequence.toString());
                    pPicSavePath.setSummary("/sdcard" + File.separator + charSequence + File.separator);
                    showToast("更新成功");
                } else {
                    showToast("更新失败");
                }

            }
        }).negativeText("取消").show();
    }


    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
