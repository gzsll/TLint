package com.gzsll.hupu.ui.post;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.PermissionData;
import com.gzsll.hupu.bean.PostData;
import com.gzsll.hupu.bean.UploadData;
import com.gzsll.hupu.bean.UploadInfo;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.SettingPrefUtil;
import com.gzsll.hupu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class PostPresenter implements PostContract.Presenter {

    private ForumApi mForumApi;
    private Context mContext;

    @Inject
    public PostPresenter(ForumApi forumApi, Context context) {
        mForumApi = forumApi;
        mContext = context;
    }

    private Subscription mSubscription;
    private PostContract.View mPostView;
    private ArrayList<String> paths = new ArrayList<>();
    private int uploadCount = 0;

    @Override
    public void checkPermission(int type, String fid, String tid) {
        mSubscription = mForumApi.checkPermission(fid, tid,
                type == Constants.TYPE_POST ? "threadPublish" : "threadReply")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PermissionData>() {
                    @Override
                    public void call(PermissionData permissionData) {
                        if (permissionData != null) {
                            if (permissionData.error != null) {
                                mPostView.renderError(permissionData.error);
                            } else if (SettingPrefUtil.isNeedExam(mContext)) {
                                mPostView.renderExam(permissionData.exam);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void parse(ArrayList<String> paths) {
        this.paths = paths;
    }

    @Override
    public void comment(final String tid, final String fid, final String pid, final String content) {
        mPostView.showLoading();
        if (paths != null && !paths.isEmpty()) {
            final List<String> images = new ArrayList<>();
            mSubscription = Observable.from(paths)
                    .flatMap(new Func1<String, Observable<UploadData>>() {
                        @Override
                        public Observable<UploadData> call(String s) {
                            return mForumApi.upload(s);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UploadData>() {
                        @Override
                        public void onStart() {
                            uploadCount = 0;
                            images.clear();
                        }

                        @Override
                        public void onCompleted() {
                            uploadCount++;
                            if (uploadCount == paths.size()) {
                                addReply(tid, fid, pid, content, images);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            uploadCount++;
                            if (uploadCount == paths.size()) {
                                addReply(tid, fid, pid, content, images);
                            }
                        }

                        @Override
                        public void onNext(UploadData uploadData) {
                            if (uploadData != null) {
                                for (UploadInfo info : uploadData.files) {
                                    images.add(info.requestUrl);
                                }
                            }
                        }
                    });
        } else {
            addReply(tid, fid, pid, content, null);
        }
    }

    private void addReply(String tid, String fid, String pid, String content, List<String> imgs) {
        StringBuilder buffer = new StringBuilder(content);
        if (imgs != null) {
            for (String url : imgs) {
                buffer.append("<br><br><img src=\"").append(url).append("\"><br><br>");
            }
        }
        System.out.println("buffer:" + buffer.toString());
        mSubscription = mForumApi.addReplyByApp(tid, fid, pid, buffer.toString())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PostData>() {
                    @Override
                    public void call(PostData data) {
                        mPostView.hideLoading();
                        if (data != null) {
                            if (data.error != null) {
                                ToastUtil.showToast(data.error.text);
                            } else if (data.status == 200) {
                                ToastUtil.showToast("发送成功~");
                                mPostView.postSuccess();
                            }
                        } else {
                            ToastUtil.showToast("您的网络有问题，请检查后重试");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mPostView.hideLoading();
                        ToastUtil.showToast("您的网络有问题，请检查后重试");
                    }
                });
    }

    @Override
    public void post(final String fid, final String content, final String title) {
        mPostView.showLoading();
        if (paths != null && !paths.isEmpty()) {
            final List<String> images = new ArrayList<>();
            mSubscription = Observable.from(paths)
                    .flatMap(new Func1<String, Observable<UploadData>>() {
                        @Override
                        public Observable<UploadData> call(String s) {
                            return mForumApi.upload(s);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UploadData>() {
                        @Override
                        public void onStart() {
                            uploadCount = 0;
                            images.clear();
                        }

                        @Override
                        public void onCompleted() {
                            uploadCount++;
                            if (uploadCount == paths.size()) {
                                addPost(fid, content, title, images);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            uploadCount++;
                            if (uploadCount == paths.size()) {
                                addPost(fid, content, title, images);
                            }
                        }

                        @Override
                        public void onNext(UploadData uploadData) {
                            if (uploadData != null) {
                                for (UploadInfo info : uploadData.files) {
                                    images.add(info.requestUrl);
                                }
                            }
                        }
                    });
        } else {
            addPost(fid, content, title, null);
        }
    }

    private void addPost(String fid, String content, String title, List<String> imgs) {
        StringBuilder buffer = new StringBuilder(content);
        if (imgs != null) {
            for (String url : imgs) {
                buffer.append("<br><br><img src=\"").append(url).append("\"><br><br>");
            }
        }
        mSubscription = mForumApi.addThread(title, buffer.toString(), fid)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PostData>() {
                    @Override
                    public void call(PostData data) {
                        mPostView.hideLoading();
                        if (data != null) {
                            if (data.error != null) {
                                ToastUtil.showToast(data.error.text);
                            } else if (data.status == 200) {
                                ToastUtil.showToast("发送成功~");
                                mPostView.postSuccess();
                            }
                        } else {
                            ToastUtil.showToast("您的网络有问题，请检查后重试");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mPostView.hideLoading();
                        ToastUtil.showToast("您的网络有问题，请检查后重试");
                    }
                });
    }

    @Override
    public void attachView(@NonNull PostContract.View view) {
        mPostView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mPostView = null;
    }
}
