package com.gzsll.hupu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.storage.bean.Folder;
import com.gzsll.hupu.storage.bean.Image;
import com.gzsll.hupu.ui.adapter.FolderAdapter;
import com.gzsll.hupu.ui.adapter.ImageAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


/**
 * Created by sll on 2015/5/19.
 */
@EActivity(R.layout.activity_gallery)
public class PhotoGalleryActivity extends BaseSwipeBackActivity {


    Logger logger = Logger.getLogger(PhotoGalleryActivity.class.getSimpleName());

    @ViewById
    GridView grid;
    @ViewById
    Toolbar toolbar;
    @Extra
    ArrayList<String> selectImages;


    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private boolean hasFolderGened = false;
    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<Folder>();

    public static final String EXTRA_RESULT = "select_result";

    private static final int MAX = 5;


    @Inject
    ImageAdapter mImageAdapter;
    @Inject
    FolderAdapter mFolderAdapter;


    @AfterViews
    void init() {
        setTitle("图库");
        initToolBar(toolbar);
        mImageAdapter.setActivity(this);
        mFolderAdapter.setActivity(this);
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        if (selectImages != null && selectImages.size() > 0) {
            resultList = selectImages;
            btCommit.setText(String.format("完成( %d/%d )", resultList.size(), MAX));
        }
        grid.setAdapter(mImageAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = (Image) parent.getAdapter().getItem(position);
                selectImageFromGrid(image, view);
            }
        });


    }


    @ViewById
    Button btCommit;


    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, View view) {
        if (image != null) {
            if (resultList.contains(image.path)) {
                resultList.remove(image.path);
                if (resultList.size() != 0) {
                    btCommit.setText(String.format("完成( %d/%d )", resultList.size(), MAX));
                } else {
                    btCommit.setText("完成");
                }

            } else {
                // 判断选择数量问题
                if (MAX == resultList.size()) {
                    Toast.makeText(this, String.format("最多选择 %d 张图片哦", MAX), Toast.LENGTH_SHORT).show();
                    return;
                }
                resultList.add(image.path);
                btCommit.setText(String.format("完成( %d/%d )", resultList.size(), MAX));
            }
            mImageAdapter.select(image, view);
        }
    }


    private String mTmpFilePath;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    @Click
    void rlCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFilePath = createTmpFile();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTmpFilePath)));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "没有系统相机", Toast.LENGTH_SHORT).show();
        }
    }

    public String createTmpFile() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!pic.exists()) {
                pic.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "tui_" + timeStamp + "";
            return pic.getAbsolutePath() + File.separator + fileName + ".jpg";
        } else {
            File cacheDir = getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "tui_" + timeStamp + "";
            return cacheDir + File.separator + fileName + ".jpg";
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logger.debug("onRestoreInstanceState");
        mTmpFilePath = savedInstanceState.getString("mTmpFilePath");
        resultList = savedInstanceState.getStringArrayList("resultList");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState");
        outState.putString("mTmpFilePath", mTmpFilePath);
        outState.putStringArrayList("resultList", resultList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        logger.debug("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (!TextUtils.isEmpty(mTmpFilePath)) {
                    resultList.add(mTmpFilePath);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                logger.debug("RESULT_OK false");
                File mTmpFile = new File(mTmpFilePath);
                if (mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        }
    }


    @Click
    void btCommit() {
        if (resultList != null && resultList.size() > 0) {
            // 返回已选择的图片数据
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(PhotoGalleryActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(PhotoGalleryActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<Image>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<Image>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(images);

                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mFolderAdapter.updateItems(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    @ViewById
    RelativeLayout rlSelect;
    @ViewById
    TextView tvFolder;

    @Click
    void tvFolder() {
        if (mFolderPopupWindow == null) {
            createPopupFolderList();
        }

        if (mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        } else {
            mFolderPopupWindow.show();
            int index = mFolderAdapter.getSelectIndex();
            index = index == 0 ? index : index - 1;
            mFolderPopupWindow.getListView().setSelection(index);
        }
    }


    private ListPopupWindow mFolderPopupWindow;

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setHeight(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setAnchorView(toolbar);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            tvFolder.setText("最近照片");
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                tvFolder.setText(folder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                        }
                        // 滑动到最初始位置
                        grid.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }


}
