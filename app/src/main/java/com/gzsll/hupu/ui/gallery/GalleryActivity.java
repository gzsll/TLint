package com.gzsll.hupu.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Folder;
import com.gzsll.hupu.bean.Image;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 * TODO 待重构
 */
public class GalleryActivity extends BaseSwipeBackActivity
        implements GalleryContract.View, ImageAdapter.OnImageItemClickListener,
        AdapterView.OnItemClickListener {

    public static void startActivity(Activity mActivity, ArrayList<String> selectImages) {
        Intent intent = new Intent(mActivity, GalleryActivity.class);
        intent.putExtra("selectImages", selectImages);
        mActivity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvFolder)
    TextView tvFolder;
    @BindView(R.id.btCommit)
    Button btCommit;

    @Inject
    GalleryPresenter mPresenter;
    @Inject
    ImageAdapter mImageAdapter;
    @Inject
    FolderAdapter mFolderAdapter;

    public static final int REQUEST_IMAGE = 101;
    public static final String EXTRA_RESULT = "select_result";

    private static final int MAX = 5;

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    private ListPopupWindow mFolderPopupWindow;

    @Override
    public int initContentView() {
        return R.layout.activity_gallery;
    }

    @Override
    public void initInjector() {
        DaggerGalleryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        setTitle("图库");
        initToolBar(toolbar);
        ArrayList<String> selectImages = getIntent().getStringArrayListExtra("selectImages");
        if (selectImages != null && selectImages.size() > 0) {
            resultList = selectImages;
            btCommit.setText(String.format("完成( %d/%d )", resultList.size(), MAX));
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mImageAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mImageAdapter.setOnImageItemClickListener(this);
        createPopupFolderList();
    }

    private void createPopupFolderList() {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setHeight(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setAnchorView(toolbar);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mFolderPopupWindow.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onImageAndFolderReceive();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    public void renderFolders(List<Folder> folders) {
        mFolderAdapter.bind(folders);
    }

    @Override
    public void renderImages(List<Image> images) {
        mImageAdapter.bind(images);
        if (resultList != null && resultList.size() > 0) {
            mImageAdapter.setDefaultSelected(resultList);
        }
    }

    @Override
    public void click(Image image, ImageView view) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mFolderAdapter.setSelectIndex(position);
        final int index = position;
        final AdapterView v = parent;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFolderPopupWindow.dismiss();
                if (index == 0) {
                    mPresenter.onImageAndFolderReceive();
                    tvFolder.setText("最近照片");
                } else {
                    Folder folder = (Folder) v.getAdapter().getItem(index);
                    if (null != folder) {
                        mImageAdapter.bind(folder.images);
                        tvFolder.setText(folder.name);
                        // 设定默认选择
                        if (resultList != null && resultList.size() > 0) {
                            mImageAdapter.setDefaultSelected(resultList);
                        }
                    }
                }
                // 滑动到最初始位置
                recyclerView.smoothScrollToPosition(0);
            }
        }, 100);
    }

    @OnClick(R.id.tvFolder)
    void setTvFolderClick() {
        if (mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        } else {
            mFolderPopupWindow.show();
            int index = mFolderAdapter.getSelectIndex();
            index = index == 0 ? index : index - 1;
            mFolderPopupWindow.getListView().setSelection(index);
        }
    }

    @OnClick(R.id.btCommit)
    void setBtCommitClick() {
        if (resultList != null && resultList.size() > 0) {
            // 返回已选择的图片数据
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private String mTmpFilePath;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    @OnClick(R.id.rlCamera)
    void setRlCameraClick() {
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
        mTmpFilePath = savedInstanceState.getString("mTmpFilePath");
        resultList = savedInstanceState.getStringArrayList("resultList");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mTmpFilePath", mTmpFilePath);
        outState.putStringArrayList("resultList", resultList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                File mTmpFile = new File(mTmpFilePath);
                if (mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
