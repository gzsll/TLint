package com.gzsll.hupu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.PostPresenter;
import com.gzsll.hupu.view.PostView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sll on 2015/3/11.
 */
@EActivity(R.layout.activity_post)
public class PostActivity extends BaseSwipeBackActivity implements PostView {

    private static final int REQUEST_IMAGE = 101;
    Logger logger = Logger.getLogger("PostActivity");

    @Extra
    int type;
    @Extra
    String fid;
    @Extra
    String tid;
    @Extra
    String title;
    @Extra
    String pid;
    @Extra
    String mTo;


    @ViewById
    EditText etSubject;
    @ViewById
    EditText etContent;
    @ViewById
    TextInputLayout textInputSubject, textInputContent;
    @ViewById
    Toolbar toolbar;
    @ViewById
    HorizontalScrollView scrollView;
    @ViewById
    LinearLayout llPics;


    @Inject
    PostPresenter postPresenter;


    private ArrayList<String> selectImages = new ArrayList<String>();
    private MaterialDialog dialog;

    @AfterViews
    void init() {
        initToolBar(toolbar);
        dialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在发送.....")
                .progress(true, 0).build();
        postPresenter.setView(this);
        postPresenter.initialize();
        if (type == Constants.TYPE_COMMENT) {
            setTitle("评论");
            etSubject.setFocusable(false);
            etSubject.setFocusableInTouchMode(false);
            etSubject.setText("Reply:" + title);
            etContent.setHint("请输入评论内容");
        } else if (type == Constants.TYPE_REPLY) {
            setTitle("评论");
            etSubject.setFocusable(false);
            etSubject.setFocusableInTouchMode(false);
            etSubject.setText("Reply:" + title);
            etContent.setHint("请输入评论内容");
        } else if (type == Constants.TYPE_AT) {
            setTitle("回复");
            etSubject.setFocusable(false);
            etSubject.setFocusableInTouchMode(false);
            etSubject.setText("回复 :" + mTo);
            etContent.setHint("请输入回复内容");
        } else if (type == Constants.TYPE_FEEDBACK) {
            setTitle("反馈");
            etSubject.setFocusable(false);
            etSubject.setFocusableInTouchMode(false);
            etSubject.setText("Feedback: TinyHuPu For Android");
            etContent.setHint("请输入反馈内容");
        } else if (type == Constants.TYPE_QUOTE) {
            setTitle("引用");
            etSubject.setFocusable(false);
            etSubject.setFocusableInTouchMode(false);
            etSubject.setText("Quote:" + title);
            etContent.setHint("请输入评论内容");
        } else {
            setTitle("发新帖");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            send();
        } else if (id == R.id.action_camera) {
            PhotoGalleryActivity_.intent(this).startForResult(REQUEST_IMAGE);
        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }


    private void send() {
        postPresenter.parse(selectImages);
        String content = etContent.getText().toString();
        if (type == Constants.TYPE_POST) {
            String title = etSubject.getText().toString();
            postPresenter.post(fid, content, title);
        } else {
            postPresenter.comment(tid, fid, pid, content);
        }

    }

    @UiThread
    @Override
    public void showLoading() {
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }

    }

    @UiThread
    @Override
    public void hideLoading() {
        if (dialog.isShowing() && !isFinishing()) {
            dialog.dismiss();
        }
    }

    @UiThread
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @UiThread(delay = 2000)
    @Override
    public void postSuccess() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            // 获取返回的图片列表
            ArrayList<String> path = data.getStringArrayListExtra(PhotoGalleryActivity.EXTRA_RESULT);
            // 处理你自己的逻辑 ....
            selectImages.clear();
            selectImages.addAll(path);
            updatePicsUI();
        }
    }

    private void updatePicsUI() {
        if (selectImages.isEmpty()) {
            scrollView.setVisibility(View.GONE);
            llPics.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            llPics.setVisibility(View.VISIBLE);
            llPics.removeAllViews();
            for (String path : selectImages) {
                View itemView = View.inflate(this, R.layout.item_post_pic, null);
                SimpleDraweeView ivPic = (SimpleDraweeView) itemView.findViewById(R.id.ivPic);
                itemView.setTag(path);
                itemView.setOnClickListener(onPictureClickListener);
                ivPic.setImageURI(Uri.fromFile(new File(path)));
                llPics.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    // 暂时只支持删除，不支持预览
    View.OnClickListener onPictureClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String path = v.getTag().toString();

            new AlertDialogWrapper.Builder(PostActivity.this)
                    .setMessage("确定删除图片")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectImages.remove(path);
                            for (int i = 0; i < llPics.getChildCount(); i++) {
                                View view = llPics.getChildAt(i);

                                if (view.getTag().toString().equals(path)) {
                                    llPics.removeView(view);
                                    break;
                                }
                            }

                            if (selectImages.isEmpty()) {
                                scrollView.setVisibility(View.GONE);
                            }
                        }

                    })
                    .show();
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postPresenter.destroy();
    }
}
