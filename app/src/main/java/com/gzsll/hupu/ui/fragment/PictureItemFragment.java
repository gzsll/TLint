package com.gzsll.hupu.ui.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gzsll.hupu.R;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;
import com.gzsll.hupu.widget.photodraweeview.OnPhotoTapListener;
import com.gzsll.hupu.widget.photodraweeview.PhotoDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;


/**
 * Created by sll on 2015/6/12.
 */
@EFragment(R.layout.preview_item_layout)
public class PictureItemFragment extends Fragment {


    @FragmentArg
    String url;
    @ViewById
    PhotoDraweeView image;
    @ViewById
    ProgressBarCircularIndeterminate progressBar;


    @AfterViews
    void init() {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();

        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setImageRequest(request);
        controller.setOldController(image.getController());
        controller.setAutoPlayAnimations(true);
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                image.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        image.setHierarchy(hierarchy);
        image.setController(controller.build());

        image.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

    }


}
