package com.gzsll.hupu.ui.imagepreview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/10.
 */
public class ImagePreviewActivity extends BaseSwipeBackActivity
        implements ViewPager.OnPageChangeListener, ImagePreviewContract.View {

    public static void startActivity(Context mContext, String extraPic, ArrayList<String> extraPics) {
        Intent intent = new Intent(mContext, ImagePreviewActivity.class);
        intent.putExtra("extraPic", extraPic);
        intent.putExtra("extraPics", extraPics);
        mContext.startActivity(intent);
    }

    @Inject
    ImagePreviewPresenter mPresenter;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private HashMap<Integer, ImageFragment> fragmentMap = new HashMap<>();
    private ImageViewAdapter mImageViewAdapter;
    private int mCurrentItem = 0;
    private List<String> extraPics;
    private String extraPic;

    @Override
    public int initContentView() {
        return R.layout.activity_preview;
    }

    @Override
    public void initInjector() {
        DaggerImagePreviewComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        initToolBar(toolbar);
        extraPics = getIntent().getStringArrayListExtra("extraPics");
        extraPic = getIntent().getStringExtra("extraPic");
        initViewPager();
        initCurrentItem();
    }

    private void initViewPager() {
        mImageViewAdapter = new ImageViewAdapter(getFragmentManager());
        viewPager.setAdapter(mImageViewAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    void initCurrentItem() {
        mCurrentItem = extraPics.indexOf(extraPic);
        if (mCurrentItem < 0) {
            mCurrentItem = 0;
        }
        viewPager.setCurrentItem(mCurrentItem);
        getSupportActionBar().setTitle((mCurrentItem + 1) + "/" + extraPics.size());
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        getSupportActionBar().setTitle((position + 1) + "/" + mImageViewAdapter.getCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ImageViewAdapter extends FragmentStatePagerAdapter {

        public ImageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImageFragment fragment = fragmentMap.get(position);
            if (fragment == null) {
                fragment = ImageFragment.newInstance(extraPics.get(position));
                fragmentMap.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return extraPics.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (ImageFragment) object);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.savePicture) {
            mPresenter.saveImage(extraPics.get(viewPager.getCurrentItem()));
        } else if (id == R.id.share) {

        } else if (id == R.id.copy) {
            mPresenter.copyImagePath(extraPics.get(viewPager.getCurrentItem()));
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.downloadAgain) {
            ImageFragment fragment =
                    ((ImageFragment) mImageViewAdapter.getItem(viewPager.getCurrentItem()));
            if (fragment != null) {
                fragment.initData();
            }
        }
        return true;
    }

    @Override
    public void setStatusBarColor(boolean on) {
        if (on) {
            StatusBarUtil.setColor(this, Color.TRANSPARENT, 0);
        }
    }
}
