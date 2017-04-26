package com.gzsll.hupu.ui.gallery;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/10.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

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
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_gallery, parent, false);
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
        holder.ivCheck.setImageResource(selectedImages.contains(image) ? R.drawable.ap_gallery_checked
                : R.drawable.ap_gallery_normal);
        int width = 100, height = 100;
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(new File(image.path)))
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();
        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(holder.ivPhoto.getController())
                        .setImageRequest(request)
                        .build();
        holder.ivPhoto.setController(controller);
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

        @BindView(R.id.ivPhoto)
        SimpleDraweeView ivPhoto;
        @BindView(R.id.ivCheck)
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
