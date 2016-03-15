package com.gzsll.hupu.presenter;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.UploadInfo;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.SecurityHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.PostView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
public class PostPresenter extends Presenter<PostView> {

    @Inject
    UserStorage mUserStorage;
    @Inject
    ForumApi mForumApi;
    @Inject
    TransferManager mTransferManager;
    @Inject
    SecurityHelper mSecurityHelper;
    @Inject
    FileHelper mFileHelper;
    @Inject
    ConfigHelper mConfigHelper;
    @Inject
    ToastHelper mToastHelper;


    @Inject
    @Singleton
    public PostPresenter() {
    }


    private ArrayList<String> paths = new ArrayList<>();
    int uploadCount = 0;


    public void parse(ArrayList<String> paths) {
        this.paths = paths;
    }

    public void comment(final String tid, final String fid, final String pid, final String content) {
        view.showLoading();
        if (paths != null && !paths.isEmpty()) {
            uploadCount = 0;
            final List<String> images = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                final UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.position = i;
                uploadInfo.uploadPath = paths.get(i);
                uploadFile(uploadInfo, new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE || progressEvent.getEventCode() == ProgressEvent.FAILED_EVENT_CODE) {
                            uploadCount++;
                        }
                        if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                            new File(uploadInfo.uploadPath).delete();
                            images.add(uploadInfo.url);
                        }
                        if (uploadCount == paths.size()) {
                            addReply(tid, fid, pid, content, images);
                        }
                    }
                });
            }

        } else {
            addReply(tid, fid, pid, content, null);
        }


    }

    private void addReply(String tid, String fid, String pid, String content, List<String> imgs) {
        StringBuffer buffer = new StringBuffer(content);
        if (imgs != null) {
            for (String url : imgs) {
                buffer.append("<br><br><img src=\"" + url + "\"><br><br>");
            }
        }
        mForumApi.addReplyByApp(tid, fid, pid, buffer.toString()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData result) {
                view.hideLoading();
                if (result != null) {
                    mToastHelper.showToast(result.msg);
                    if (result.status == 200) {
                        view.postSuccess();
                    }
                } else {
                    mToastHelper.showToast("您的网络有问题，请检查后重试");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                mToastHelper.showToast("您的网络有问题，请检查后重试");
            }
        });
    }


    public void post(final String fid, final String content, final String title) {
        view.showLoading();
        if (paths != null && !paths.isEmpty()) {
            uploadCount = 0;
            final List<String> images = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                final UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.position = i;
                uploadInfo.uploadPath = paths.get(i);
                uploadFile(uploadInfo, new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE || progressEvent.getEventCode() == ProgressEvent.FAILED_EVENT_CODE) {
                            uploadCount++;
                        }
                        if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                            new File(uploadInfo.uploadPath).delete();
                            images.add(uploadInfo.url);
                        }
                        if (uploadCount == paths.size()) {
                            addPost(fid, content, title, images);
                        }
                    }
                });
            }

        } else {
            addPost(fid, content, title, null);
        }
    }

    private void addPost(String fid, String content, String title, List<String> imgs) {
        StringBuffer buffer = new StringBuffer(content);
        if (imgs != null) {
            for (String url : imgs) {
                buffer.append("<br><br><img src=\"" + url + "\"><br><br>");
            }
        }
        mForumApi.addThread(title, buffer.toString(), fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData result) {
                view.hideLoading();
                if (result != null) {
                    mToastHelper.showToast(result.msg);
                    if (result.status == 200) {
                        view.postSuccess();
                    }
                } else {
                    mToastHelper.showToast("您的网络有问题，请检查后重试");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                mToastHelper.showToast("您的网络有问题，请检查后重试");
            }
        });
    }


    private List<PutObjectRequest> requests = new ArrayList<>();

    public Upload uploadFile(UploadInfo uploadInfo, ProgressListener progressListener) {
        File file = new File(uploadInfo.uploadPath);
        StringBuilder builder = new StringBuilder();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uploadInfo.uploadPath, options);
        int width = options.outWidth;
        int height = options.outHeight;

        String uid = mUserStorage.getUid();
        if (!TextUtils.isEmpty(uid)) {
            builder.append(uid);
            builder.append("_");
        }
        builder.append("byte");
        builder.append(mFileHelper.length(file.length()));
        builder.append("_");
        builder.append(mSecurityHelper.getMd5ByteByFile(file));
        builder.append("_hupu_android_w");
        builder.append(width);
        builder.append("h");
        builder.append(height);
        if (uploadInfo.uploadPath.endsWith(".gif")) {
            builder.append(".gif");
        } else {
            builder.append(".png");
        }
        File uploadFile = new File(mConfigHelper.getUploadPath() + builder.toString());
        mFileHelper.copy(file, uploadFile);
        PutObjectRequest withGeneralProgressListener = new PutObjectRequest(Constants.BOX_BUCKET_NAME, uploadFile.getName(), uploadFile).withCannedAcl(CannedAccessControlList.PublicRead).withGeneralProgressListener(progressListener);
        uploadInfo.url = Constants.BOX_END_POINT_NEW + uploadFile.getName();
        Upload upload = mTransferManager.upload(withGeneralProgressListener);
        requests.add(withGeneralProgressListener);
        return upload;
    }


    @Override
    public void detachView() {
        paths.clear();
    }
}
