package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzsll.hupu.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/9/17.
 */
public abstract class BaseListAdapter<T, V extends View> extends BaseAdapter {


    private List<T> items = new ArrayList<>();
    protected BaseActivity mActivity;

    public void updateItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V view;
        if (convertView == null) {
            view = onCreateItemView(parent.getContext());
        } else {
            view = (V) convertView;
        }
        onBindView(view, position, items.get(position));
        return view;
    }

    protected abstract V onCreateItemView(Context context);

    protected abstract void onBindView(V view, int position, T data);

    public List<T> getItems() {
        return items;
    }


    public BaseActivity getActivity() {
        return mActivity;
    }

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
