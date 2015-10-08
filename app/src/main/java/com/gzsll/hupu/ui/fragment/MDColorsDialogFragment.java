package com.gzsll.hupu.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.pref.SettingPref_;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.utils.ThemeHelper;
import com.squareup.otto.Bus;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * 界面配色设置
 *
 * @author wangdan
 */
@EFragment
public class MDColorsDialogFragment extends DialogFragment
        implements OnItemClickListener {

    @Inject
    ThemeHelper mThemeHelper;
    @Pref
    SettingPref_ mSettingPref;
    @Inject
    Bus mBus;

    public static void launch(Activity context) {
        Fragment fragment = context.getFragmentManager().findFragmentByTag("DialogFragment");
        if (fragment != null) {
            context.getFragmentManager().beginTransaction().remove(fragment).commit();
        }

        MDColorsDialogFragment dialogFragment = MDColorsDialogFragment_.builder().build();
        dialogFragment.show(context.getFragmentManager(), "DialogFragment");
    }


    private Map<String, ColorDrawable> colorMap = new HashMap<String, ColorDrawable>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        ((AppApplication) getActivity().getApplicationContext()).inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        View view = View.inflate(getActivity(), R.layout.dialog_md_colors, null);
        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new MDColorsAdapter());
        gridView.setOnItemClickListener(this);
        return new AlertDialogWrapper.Builder(getActivity())
                .setView(view)
                .setPositiveButton("取消", null)
                .create();
    }

    class MDColorsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mThemeHelper.themeColorArr.length;
        }

        @Override
        public Object getItem(int position) {
            return mThemeHelper.themeColorArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = View.inflate(getActivity(), R.layout.item_md_colors, null);

            if (!colorMap.containsKey(String.valueOf(position)))
                colorMap.put(String.valueOf(position), new ColorDrawable(getResources().getColor(mThemeHelper.themeColorArr[position][0])));

            ImageView imgColor = (ImageView) convertView.findViewById(R.id.ivColor);
            ColorDrawable colorDrawable = colorMap.get(String.valueOf(position));
            imgColor.setImageDrawable(colorDrawable);

            View imgSelected = convertView.findViewById(R.id.ivSelected);
            imgSelected.setVisibility(mSettingPref.ThemeIndex().get() == position ? View.VISIBLE : View.GONE);

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mSettingPref.ThemeIndex().get()) {
            dismiss();

            return;
        }

        mSettingPref.ThemeIndex().put(position);

        dismiss();
        mBus.post(new ChangeThemeEvent());
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).reload();

    }


}
