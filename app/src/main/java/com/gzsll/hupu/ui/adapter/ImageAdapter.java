package com.gzsll.hupu.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.storage.bean.Image;
import com.gzsll.hupu.ui.activity.PhotoGalleryActivity;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/5/19.
 */
public class ImageAdapter extends BaseAdapter {

    @Inject
    LayoutInflater mInflater;


    Logger logger = Logger.getLogger(ImageAdapter.class.getSimpleName());
    private PhotoGalleryActivity mActivity;


    private List<Image> mImages = new ArrayList<Image>();
    private List<Image> mSelectedImages = new ArrayList<Image>();

    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }


    /**
     * 选择某个图片，改变选择状态
     *
     * @param image
     */
    public void select(Image image, View view) {
        ImageAdapter.ViewHolder h = (ImageAdapter.ViewHolder) view.getTag();
        image.checked = !image.checked;
        if (image.checked) {
            mSelectedImages.add(image);
            h.check.setImageResource(R.drawable.ap_gallery_checked);
        } else {
            mSelectedImages.remove(image);
            h.check.setImageResource(R.drawable.ap_gallery_normal);
        }
    }

    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }


    private Image getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (Image image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Image getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_grid_gallery, parent, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bindData(getItem(position));
        return view;
    }

    public void setActivity(PhotoGalleryActivity mActivity) {
        this.mActivity = mActivity;
    }

    public class ViewHolder {
        SimpleDraweeView image;
        ImageView check;

        ViewHolder(View view) {
            image = (SimpleDraweeView) view.findViewById(R.id.ivPhoto);
            check = (ImageView) view.findViewById(R.id.ivCheck);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态
            check.setVisibility(View.VISIBLE);
            if (mSelectedImages.contains(data)) {
                // 设置选中状态
                check.setImageResource(R.drawable.ap_gallery_checked);
            } else {
                // 未选择
                check.setImageResource(R.drawable.ap_gallery_normal);
            }

            // 显示图片
            image.setAspectRatio(1);
            image.setImageURI(Uri.fromFile(new File(data.path)));
        }
    }
}
