package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.FormatHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.ImageFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
public class ImagePreviewActivity extends BaseSwipeBackActivity implements ViewPager.OnPageChangeListener {


    public static void startActivity(Context mContext, String extraPic, ArrayList<String> extraPics) {
        Intent intent = new Intent(mContext, ImagePreviewActivity.class);
        intent.putExtra("extraPic", extraPic);
        intent.putExtra("extraPics", extraPics);
        mContext.startActivity(intent);
    }

    @Inject
    FormatHelper mFormatHelper;
    @Inject
    OkHttpHelper mOkHttpHelper;
    @Inject
    FileHelper mFileHelper;
    @Inject
    ConfigHelper mConfigHelper;
    @Inject
    ToastHelper mToastHelper;


    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private HashMap<Integer, ImageFragment> fragmentMap
            = new HashMap<>();
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
        mActivityComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        initToolBar(toolbar);
        extraPics = getIntent().getStringArrayListExtra("extraPics");
        extraPic = getIntent().getStringExtra("extraPic");
        initViewPager();
        initCurrentItem();
    }

    private void initViewPager() {
        mImageViewAdapter = new ImageViewAdapter(getSupportFragmentManager());
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
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected int configTheme() {
        return R.style.AppTheme_Pics;
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

    public class ImageViewAdapter extends FragmentPagerAdapter {


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
            save(extraPics.get(viewPager.getCurrentItem()));
        } else if (id == R.id.share) {

        } else if (id == R.id.copy) {

        } else if (id == R.id.downloadAgain) {

        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void save(String url) {
        Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                String fileName = mFormatHelper.getFileNameFromUrl(s);
                File target = new File(mConfigHelper.getPicSavePath(), fileName);
                if (!target.exists()) {
                    try {
                        mOkHttpHelper.httpDownload(s, target);
                        scanPhoto(target);
                        return "保存成功";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "保存失败";
                    }
                } else {
                    return "图片已存在";
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mToastHelper.showToast(s);
            }
        });
    }

    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


}
