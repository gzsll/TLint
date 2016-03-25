package com.gzsll.hupu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by sll on 2016/3/10.
 */
public class ImageFragment extends BaseFragment {

    @Bind(R.id.image)
    ImageView image;
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
        Glide.with(this).load(url).into(image);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
    }


}
