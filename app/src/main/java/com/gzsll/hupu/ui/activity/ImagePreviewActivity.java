package com.gzsll.hupu.ui.activity;

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
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.pref.SettingPref_;
import com.gzsll.hupu.support.utils.FileHelper;
import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.ui.fragment.PictureItemFragment;
import com.gzsll.hupu.ui.fragment.PictureItemFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/5/4.
 */
@EActivity(R.layout.activity_preview)
public class ImagePreviewActivity extends BaseSwipeBackActivity implements ViewPager.OnPageChangeListener {

    Logger logger = Logger.getLogger(ImagePreviewActivity.class.getSimpleName());

    @Extra
    List<String> extraPics;
    @Extra
    String extraPic;

    @Pref
    SettingPref_ mSettingPref;

    @ViewById
    ViewPager viewPager;
    @ViewById
    Toolbar toolbar;

    @Inject
    FormatHelper formatHelper;
    @Inject
    OkHttpHelper okHttpHelper;
    @Inject
    FileHelper mFileHelper;

    private HashMap<Integer, PictureItemFragment> fragmentMap
            = new HashMap<Integer, PictureItemFragment>();
    private ImageViewAdapter mImageViewAdapter;
    private int mCurrentItem = 0;


    @AfterViews
    void init() {
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        initToolBar(toolbar);
        initViewPager();
        initCurrentItem();


    }

    @Override
    protected int configTheme() {
        return R.style.AppTheme_Pics;
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


    public class ImageViewAdapter extends FragmentPagerAdapter {


        public ImageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PictureItemFragment fragment = fragmentMap.get(position);
            if (fragment == null) {
                fragment = PictureItemFragment_.builder().url(extraPics.get(position)).build();
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
                fragmentMap.put(position, (PictureItemFragment) object);
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

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }


    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Background
    void save(String url) {
        String fileName = formatHelper.getFileNameFromUrl(url);
        String path = mFileHelper.getSdcardPath() + File.separator + mSettingPref.PicSavePath().get() + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File target = new File(path, fileName);
        if (!target.exists()) {
            try {
                okHttpHelper.httpDownload(url, target);
                scanPhoto(target);
                showToast("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                showToast("保存失败");
            }
        } else {
            showToast("图片已存在");
        }


    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
}
