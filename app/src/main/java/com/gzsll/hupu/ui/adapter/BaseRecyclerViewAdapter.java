package com.gzsll.hupu.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gzsll.hupu.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/3/7.
 */
public abstract class BaseRecyclerViewAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items = new ArrayList<T>();
    protected BaseActivity mActivity;

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    public void updateItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    // additional methods to manipulate the items

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }
}