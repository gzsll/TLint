package com.gzsll.hupu.ui.setting;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.gzsll.hupu.R;
import com.gzsll.hupu.injector.HasComponent;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.util.SettingPrefUtil;
import com.gzsll.hupu.util.ThemeUtil;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by sll on 2016/5/17.
 */
public class ColorsDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener {

    public static void launch(Activity activity) {
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("DialogFragment");
        if (fragment != null) {
            activity.getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        ColorsDialogFragment dialogFragment = new ColorsDialogFragment();
        dialogFragment.show(activity.getFragmentManager(), "DialogFragment");
    }

    @Inject
    Bus mBus;

    private Map<String, ColorDrawable> colorMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingComponent.class.cast(((HasComponent<SettingComponent>) getActivity()).getComponent())
                .inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        View view = View.inflate(getActivity(), R.layout.dialog_md_colors, null);
        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new MDColorsAdapter());
        gridView.setOnItemClickListener(this);
        return new AlertDialogWrapper.Builder(getActivity()).setView(view)
                .setPositiveButton("取消", null)
                .create();
    }

    class MDColorsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ThemeUtil.themeColorArr.length;
        }

        @Override
        public Object getItem(int position) {
            return ThemeUtil.themeColorArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_md_colors, null);
            }

            if (!colorMap.containsKey(String.valueOf(position))) {
                colorMap.put(String.valueOf(position),
                        new ColorDrawable(getResources().getColor(ThemeUtil.themeColorArr[position][0])));
            }

            ImageView imgColor = (ImageView) convertView.findViewById(R.id.ivColor);
            ColorDrawable colorDrawable = colorMap.get(String.valueOf(position));
            imgColor.setImageDrawable(colorDrawable);

            View imgSelected = convertView.findViewById(R.id.ivSelected);
            imgSelected.setVisibility(
                    SettingPrefUtil.getThemeIndex(getActivity()) == position ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == SettingPrefUtil.getThemeIndex(getActivity())) {
            dismiss();
            return;
        }
        SettingPrefUtil.setThemeIndex(getActivity(), position);
        dismiss();
        mBus.post(new ChangeThemeEvent());
        if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).reload();
    }
}
