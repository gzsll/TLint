package com.gzsll.hupu.ui.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
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
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;
import com.gzsll.hupu.widget.photodraweeview.OnPhotoTapListener;
import com.gzsll.hupu.widget.photodraweeview.PhotoDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/10.
 */
public class ImageFragment extends BaseFragment {

    @Bind(R.id.image)
    PhotoDraweeView image;
    @Bind(R.id.progressBar)
    ProgressBarCircularIndeterminate progressBar;

    public static ImageFragment newInstance(String url) {
        ImageFragment mFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    private String url;


    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.preview_item_layout;
    }

    @Override
    public void getBundle(Bundle bundle) {
        url = bundle.getString("url");
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        showContent(true);
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
