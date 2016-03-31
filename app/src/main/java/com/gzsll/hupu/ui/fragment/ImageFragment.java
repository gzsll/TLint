package com.gzsll.hupu.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.gzsll.hupu.R;
import com.gzsll.hupu.components.glide.GifDrawableImageViewTarget;
import com.gzsll.hupu.components.glide.ProgressTarget;
import com.gzsll.hupu.helper.ResourceHelper;
import com.gzsll.hupu.ui.BaseLazyLoadFragment;
import com.gzsll.hupu.widget.CircleProgressBar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by sll on 2016/3/10.
 */
public class ImageFragment extends BaseLazyLoadFragment {

    @Bind(R.id.image)
    PhotoView image;
    @Bind(R.id.progressBar)
    CircleProgressBar progressBar;
    @Bind(R.id.progress)
    SmoothProgressBar progress;
    @Bind(R.id.rlProgress)
    RelativeLayout rlProgress;

    @Inject
    ResourceHelper mResourceHelper;

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
        mFragmentComponent.inject(this);
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
        progressBar.setMax(100);
        progressBar.setProgressColor(mResourceHelper.getThemeColor(getActivity()));
        progress.setIndeterminate(true);
        image.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rlProgress.setPadding(0, mResourceHelper.getStatusBarHeight(getActivity()), 0, 0);
        }
    }

    @Override
    public void initData() {
        showContent(true);
        if (url.endsWith(".gif")) {
            ProgressTarget<String, GifDrawable> target = new MyProgressTarget<>(new GifDrawableImageViewTarget(image), progress, progressBar);
            target.setModel(url);
            Glide.with(this).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(target);
        } else {
            ProgressTarget<String, Bitmap> target = new MyProgressTarget<>(new BitmapImageViewTarget(image), progress, progressBar);
            target.setModel(url);
            Glide.with(this).load(url).asBitmap().fitCenter().into(target);
        }
    }


    private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {

        private final SmoothProgressBar mSmoothProgressBar;
        private final CircleProgressBar mCircleProgressBar;

        public MyProgressTarget(Target<Z> target, SmoothProgressBar mSmoothProgressBar, CircleProgressBar mCircleProgressBar) {
            super(target);
            this.mSmoothProgressBar = mSmoothProgressBar;
            this.mCircleProgressBar = mCircleProgressBar;
        }

        @Override
        protected void onConnecting() {
            System.out.println("onConnecting");
            mCircleProgressBar.setVisibility(View.GONE);
            mSmoothProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onDownloading(long bytesRead, long expectedLength) {
            int progress = (int) (100 * bytesRead / expectedLength);
            mCircleProgressBar.setProgress(progress);
            if (progress > 1) {
                mSmoothProgressBar.setVisibility(View.GONE);
                mCircleProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onDownloaded() {
            System.out.println("onDownloaded");

        }

        @Override
        protected void onDelivered() {
            System.out.println("onDelivered");
            mCircleProgressBar.setVisibility(View.GONE);
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    }


}
