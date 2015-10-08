package com.gzsll.hupu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import com.gzsll.hupu.storage.bean.Folder;
import com.gzsll.hupu.ui.activity.PhotoGalleryActivity;
import com.gzsll.hupu.ui.view.FolderItemView;
import com.gzsll.hupu.ui.view.FolderItemView_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/5/19.
 */
public class FolderAdapter extends BaseListAdapter<Folder, FolderItemView> {


    @Inject
    LayoutInflater mInflater;

    private PhotoGalleryActivity mActivity;

    int lastSelected = 0;


    public void setActivity(PhotoGalleryActivity mActivity) {
        this.mActivity = mActivity;
    }


    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    @Override
    protected FolderItemView onCreateItemView(Context context) {
        return FolderItemView_.build(context);
    }

    @Override
    protected void onBindView(FolderItemView view, int position, Folder data) {
        view.init(getItems(), position, lastSelected);
    }
}
