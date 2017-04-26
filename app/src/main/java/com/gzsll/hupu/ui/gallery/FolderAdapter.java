package com.gzsll.hupu.ui.gallery;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class FolderAdapter extends BaseAdapter {

    private List<Folder> folders = new ArrayList<>();
    private int lastSelected = 0;

    @Inject
    public FolderAdapter() {
    }

    public void bind(List<Folder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private int getTotalImageSize(List<Folder> mFolders) {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Folder getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_folder, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Folder folder = folders.get(position);
        if (position == 0) {
            holder.tvName.setText("所有图片");
            holder.tvSize.setText(getTotalImageSize(folders) + "张");
            if (!folders.isEmpty()) {
                holder.ivCover.setImageURI(Uri.fromFile(new File(folder.cover.path)));
            }
        } else {
            holder.tvName.setText(folder.name);
            holder.tvSize.setText(folder.images.size() + "张");
            holder.ivCover.setImageURI(Uri.fromFile(new File(folder.cover.path)));
        }
        holder.ivIndicator.setVisibility(lastSelected == position ? View.VISIBLE : View.GONE);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivCover)
        SimpleDraweeView ivCover;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvSize)
        TextView tvSize;
        @BindView(R.id.ivIndicator)
        ImageView ivIndicator;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
