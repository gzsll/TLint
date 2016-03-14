package com.gzsll.hupu.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.helper.ThemeHelper;
import com.gzsll.hupu.injector.component.DaggerFragmentComponent;
import com.gzsll.hupu.injector.module.FragmentModule;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.ui.BaseActivity;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class MDColorsDialogFragment extends DialogFragment
        implements OnItemClickListener {

    @Inject
    ThemeHelper mThemeHelper;
    @Inject
    SettingPrefHelper mSettingPref;
    @Inject
    Bus mBus;

    public static void launch(FragmentActivity context) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag("DialogFragment");
        if (fragment != null) {
            context.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        MDColorsDialogFragment dialogFragment = new MDColorsDialogFragment();
        dialogFragment.show(context.getSupportFragmentManager(), "DialogFragment");
    }


    private Map<String, ColorDrawable> colorMap = new HashMap<String, ColorDrawable>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(((MyApplication) getActivity().getApplication()).getApplicationComponent())
                .build().inject(this);
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
            imgSelected.setVisibility(mSettingPref.getThemeIndex() == position ? View.VISIBLE : View.GONE);

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mSettingPref.getThemeIndex()) {
            dismiss();

            return;
        }

        mSettingPref.setThemeIndex(position);

        dismiss();
        mBus.post(new ChangeThemeEvent());
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).reload();

    }


}
