package com.gzsll.hupu.ui.post;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.BaseError;
import com.gzsll.hupu.bean.Exam;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.gallery.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
public class PostActivity extends BaseSwipeBackActivity implements PostContract.View {

    public static void startActivity(Context mContext, int type, String fid, String tid, String pid,
                                     String title) {
        Intent intent = new Intent(mContext, PostActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("fid", fid);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etSubject)
    EditText etSubject;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.llPics)
    LinearLayout llPics;
    @BindView(R.id.scrollView)
    HorizontalScrollView scrollView;

    private ArrayList<String> selectImages = new ArrayList<String>();
    private MaterialDialog mDialog;

    private String title;
    private int type;
    private String fid;
    private String tid;
    private String pid;

    @Inject
    PostPresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_post;
    }

    @Override
    public void initInjector() {
        DaggerPostComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(toolbar);
        initBundle();
        initPostType();
        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkPermission(type, fid, tid);
    }

    private void initDialog() {
        mDialog =
                new MaterialDialog.Builder(this).title("提示").content("正在发送").progress(true, 0).build();
    }

    private void initBundle() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", Constants.TYPE_COMMENT);
        title = intent.getStringExtra("title");
        fid = intent.getStringExtra("fid");
        tid = intent.getStringExtra("tid");
        pid = intent.getStringExtra("pid");
    }

    private void initPostType() {
        switch (type) {
            case Constants.TYPE_COMMENT:
                setTitle("评论");
                etSubject.setFocusable(false);
                etSubject.setFocusableInTouchMode(false);
                etSubject.setText("Reply:" + title);
                etContent.setHint("请输入评论内容");
                break;
            case Constants.TYPE_REPLY:
                setTitle("评论");
                etSubject.setFocusable(false);
                etSubject.setFocusableInTouchMode(false);
                etSubject.setText("Reply:" + title);
                etContent.setHint("请输入评论内容");
                break;
            case Constants.TYPE_AT:
                setTitle("评论");
                etSubject.setFocusable(false);
                etSubject.setFocusableInTouchMode(false);
                etSubject.setText("Reply:" + title);
                etContent.setHint("请输入评论内容");
                break;
            case Constants.TYPE_FEEDBACK:
                setTitle("反馈");
                etSubject.setFocusable(false);
                etSubject.setFocusableInTouchMode(false);
                etSubject.setText("Feedback: TLint For Android");
                etContent.setHint("请输入反馈内容");
                break;
            case Constants.TYPE_QUOTE:
                setTitle("引用");
                etSubject.setFocusable(false);
                etSubject.setFocusableInTouchMode(false);
                etSubject.setText("Quote:" + title);
                etContent.setHint("请输入评论内容");
                break;
            default:
                setTitle("发新帖");
                break;
        }
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
    public void renderError(BaseError error) {
        if (error != null) {
            System.out.println(error.text);
            new MaterialDialog.Builder(this).title("温馨提示")
                    .content(error.text)
                    .cancelable(false)
                    .positiveText("确定")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void renderExam(final Exam exam) {
        if (exam != null) {
            new MaterialDialog.Builder(this).title("温馨提示")
                    .content(exam.title)
                    .cancelable(false)
                    .positiveText("开始答题")
                    .negativeText("放弃答题")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            BrowserActivity.startActivity(PostActivity.this, exam.url, false);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void showLoading() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void postSuccess() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                finish();
            }
        });
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
            GalleryActivity.startActivity(this, selectImages);
        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void send() {
        mPresenter.parse(selectImages);
        String content = etContent.getText().toString();
        if (type == Constants.TYPE_POST) {
            String title = etSubject.getText().toString();
            mPresenter.post(fid, content, title);
        } else {
            mPresenter.comment(tid, fid, pid, content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryActivity.REQUEST_IMAGE && resultCode == RESULT_OK) {
            // 获取返回的图片列表
            ArrayList<String> path = data.getStringArrayListExtra(GalleryActivity.EXTRA_RESULT);
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
                llPics.addView(itemView,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    // 暂时只支持删除，不支持预览
    View.OnClickListener onPictureClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String path = v.getTag().toString();

            new AlertDialogWrapper.Builder(PostActivity.this).setMessage("确定删除图片")
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
        mPresenter.detachView();
    }
}
