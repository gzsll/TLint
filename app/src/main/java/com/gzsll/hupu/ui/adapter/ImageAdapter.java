package com.gzsll.hupu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/10.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    @Inject
    Activity mActivity;

    private int imageSize = 0;

    @Inject
    public ImageAdapter() {
    }

    private List<Image> images = new ArrayList<>();
    private List<Image> selectedImages = new ArrayList<>();
    private OnImageItemClickListener mOnImageItemClickListener;


    public void bind(List<Image> images) {
        selectedImages.clear();
        if (images != null && images.size() > 0) {
            this.images = images;
        } else {
            this.images.clear();
        }
        notifyDataSetChanged();
    }


    public void select(Image image, ImageView ivCheck) {
        image.checked = !image.checked;
        if (image.checked) {
            selectedImages.add(image);
            ivCheck.setImageResource(R.drawable.ap_gallery_checked);
        } else {
            selectedImages.remove(image);
            ivCheck.setImageResource(R.drawable.ap_gallery_normal);
        }
    }


    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                selectedImages.add(image);
            }
        }
        if (selectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }


    private Image getImageByPath(String path) {
        if (images != null && images.size() > 0) {
            for (Image image : images) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_gallery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = images.get(position);
        holder.image = image;
        if (image == null) {
            return;
        }
        holder.ivCheck.setVisibility(View.VISIBLE);
        holder.ivCheck.setImageResource(selectedImages.contains(image) ? R.drawable.ap_gallery_checked : R.drawable.ap_gallery_normal);
        if (imageSize == 0) {
            WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int widthPixels = metrics.widthPixels;
            imageSize = widthPixels / 4;
        }

        Glide.with(mActivity).load(new File(image.path)).dontAnimate()
                .thumbnail(0.5f).override(imageSize, imageSize).centerCrop().into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setOnImageItemClickListener(OnImageItemClickListener mOnImageItemClickListener) {
        this.mOnImageItemClickListener = mOnImageItemClickListener;
    }

    public interface OnImageItemClickListener {
        void click(Image image, ImageView view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.ivPhoto)
        ImageView ivPhoto;
        @Bind(R.id.ivCheck)
        ImageView ivCheck;
        Image image;


        @OnClick(R.id.flItem)
        void flItemClick() {
            if (mOnImageItemClickListener != null) {
                mOnImageItemClickListener.click(image, ivCheck);
            }
        }


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}
