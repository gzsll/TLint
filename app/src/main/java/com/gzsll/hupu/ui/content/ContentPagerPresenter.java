package com.gzsll.hupu.ui.content;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.BaseError;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.data.ContentRepository;
import com.gzsll.hupu.db.ImageCache;
import com.gzsll.hupu.db.ImageCacheDao;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.otto.UpdateContentPageEvent;
import com.gzsll.hupu.provider.LocalImageProvider;
import com.gzsll.hupu.util.ConfigUtil;
import com.gzsll.hupu.util.FileUtil;
import com.gzsll.hupu.util.FormatUtil;
import com.gzsll.hupu.util.ToastUtil;
import com.squareup.otto.Bus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sll on 2016/5/25.
 */
public class ContentPagerPresenter implements ContentPagerContract.Presenter {

    private ContentRepository mContentRepository;
    private ForumApi mForumApi;
    private Bus mBus;
    private ImageCacheDao mImageCacheDao;
    private OkHttpHelper mOkHttpHelper;
    private UserStorage mUserStorage;

    private ContentPagerContract.View mContentView;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private ConcurrentHashMap<String, String> imageMap = new ConcurrentHashMap<>();
    private List<String> taskArray = new ArrayList<>();

    private List<ThreadReply> lightReplies = new ArrayList<>();
    private List<ThreadReply> replies = new ArrayList<>();
    private String fid;
    private String tid;
    private int page;

    @Inject
    public ContentPagerPresenter(ContentRepository mContentRepository, ForumApi mForumApi, Bus mBus,
                                 ImageCacheDao mImageCacheDao, OkHttpHelper mOkHttpHelper, UserStorage mUserStorage) {
        this.mContentRepository = mContentRepository;
        this.mForumApi = mForumApi;
        this.mBus = mBus;
        this.mImageCacheDao = mImageCacheDao;
        this.mOkHttpHelper = mOkHttpHelper;
        this.mUserStorage = mUserStorage;
    }

    @Override
    public void onThreadInfoReceive(final String tid, final String fid, String pid, final int page) {
        this.fid = fid;
        this.tid = tid;
        this.page = page;
        if (page == 1) {
            Subscription mSubscription = mContentRepository.getThreadInfo(fid, tid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ThreadInfo>() {
                        @Override
                        public void call(ThreadInfo threadInfo) {
                            if (threadInfo.getError() != null) {
                                BaseError error = threadInfo.getError();
                                ToastUtil.showToast(error.text);
                                mContentView.onClose();
                            } else {
                                mContentView.sendMessageToJS("addThreadInfo", threadInfo);
                                loadLightReplies(tid, fid);
                                loadReplies(tid, fid, page);
                                mContentView.hideLoading();
                                mBus.post(
                                        new UpdateContentPageEvent(threadInfo.getPage(), threadInfo.getTotalPage()));
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            mContentView.onError();
                        }
                    });
            mCompositeSubscription.add(mSubscription);
        } else {
            loadReplies(tid, fid, page);
        }
    }

    @Override
    public void onReply(int area, int index) {
        if (!isLogin()) {
            return;
        }
        ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
        mContentView.showReplyUi(fid, tid, reply.getPid(), reply.getContent());
    }

    @Override
    public void onReport(int area, int index) {
        if (!isLogin()) {
            return;
        }
        ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
        mContentView.showReportUi(tid, reply.getPid());
    }

    @Override
    public void addLight(int area, int index) {
        if (!isLogin()) {
            return;
        }
        final ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
        mForumApi.addLight(tid, fid, reply.getPid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData baseData) {
                        if (baseData != null) {
                            if (baseData.status == 200) {
                                AddLight light = new AddLight(baseData.result, reply.getPid());
                                mContentView.sendMessageToJS("addLight", light);
                                ToastUtil.showToast("点亮成功");
                            } else if (baseData.error != null) {
                                ToastUtil.showToast(baseData.error.text);
                            }
                        } else {
                            ToastUtil.showToast("点亮失败，请检查网络后重试");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast("点亮失败，请检查网络后重试");
                    }
                });
    }

    @Override
    public void addRuLight(int area, int index) {
        if (!isLogin()) {
            return;
        }
        final ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
        mForumApi.addRuLight(tid, fid, reply.getPid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData baseData) {
                        if (baseData != null) {
                            if (baseData.status == 200) {
                                AddLight light = new AddLight(baseData.result, reply.getPid());
                                mContentView.sendMessageToJS("addLight", light);
                                ToastUtil.showToast("点灭成功");
                            } else if (baseData.error != null) {
                                ToastUtil.showToast(baseData.error.text);
                            }
                        } else {
                            ToastUtil.showToast("点灭失败，请检查网络后重试");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast("点灭失败，请检查网络后重试");
                    }
                });
    }

    private boolean isLogin() {
        if (!mUserStorage.isLogin()) {
            ToastUtil.showToast("请先登录!!");
            mContentView.showLoginUi();
            return false;
        }
        return true;
    }

    @Override
    public void handlerUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (scheme.startsWith("http")) {
                mContentView.showBrowserUi(url);
            } else if (scheme.equalsIgnoreCase("kanqiu")) {
                if (url.contains("topic")) {
                    String tid = uri.getLastPathSegment();
                    String page = uri.getQueryParameter("page");
                    String pid = uri.getQueryParameter("pid");
                    mContentView.showContentUi(tid, pid, TextUtils.isEmpty(page) ? 1 : Integer.valueOf(page));
                } else if (url.contains("board")) {
                    String boardId = url.substring(url.lastIndexOf("/") + 1);
                    mContentView.showThreadListUi(boardId);
                } else if (url.contains("people")) {
                    String uid = url.substring(url.lastIndexOf("/") + 1);
                    mContentView.showUserProfileUi(uid);
                }
            }
        }
    }

    @Override
    public void onReload() {
        mContentView.showLoading();
        onThreadInfoReceive(tid, fid, "", page);
    }

    @Override
    public HupuBridge getJavaScriptInterface() {
        return new HupuBridge();
    }

    public class AddLight {

        public int light;
        public String pid;

        public AddLight(int light, String pid) {
            this.light = light;
            this.pid = pid;
        }
    }

    private void loadLightReplies(String tid, String fid) {
        Subscription mSubscription = mContentRepository.getLightReplies(fid, tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ThreadReply>>() {
                    @Override
                    public void call(List<ThreadReply> threadReplies) {
                        lightReplies = threadReplies;
                        if (!threadReplies.isEmpty()) {
                            mContentView.sendMessageToJS("addLightTitle", "\"这些回帖亮了\"");
                            for (int i = 0; i < threadReplies.size(); i++) {
                                ThreadReply reply = threadReplies.get(i);
                                reply.setIndex(i);
                                mContentView.sendMessageToJS("addLightPost", reply);
                            }
                            mContentView.loadUrl("javascript:reloadLightStuff();");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mContentView.onError();
                    }
                });
        mCompositeSubscription.add(mSubscription);
    }

    private void loadReplies(String tid, String fid, final int page) {
        Subscription mSubscription = mContentRepository.getReplies(fid, tid, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ThreadReply>>() {
                    @Override
                    public void call(List<ThreadReply> threadReplies) {
                        replies = threadReplies;
                        if (page == 1) {
                            mContentView.sendMessageToJS("addReplyTitle", "\"全部回帖\"");
                        }
                        if (threadReplies.isEmpty()) {
                            mContentView.loadUrl("javascript:addReplyEmpty();");
                        } else {
                            for (int i = 0; i < threadReplies.size(); i++) {
                                ThreadReply reply = threadReplies.get(i);
                                reply.setIndex(i);
                                mContentView.sendMessageToJS("addReply", reply);
                            }
                        }
                        mContentView.loadUrl("javascript:reloadReplyStuff();");
                        mContentView.hideLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mContentView.onError();
                    }
                });
        mCompositeSubscription.add(mSubscription);
    }

    @Override
    public void attachView(@NonNull ContentPagerContract.View view) {
        mContentView = view;
        mContentView.showLoading();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
        mContentView = null;
    }

    public class HupuBridge {

        @JavascriptInterface
        public String replaceImage(final String imageUrl, final int index) {
            if (imageMap.contains(imageUrl)) {
                return LocalImageProvider.constructUri(imageMap.get(imageUrl));
            } else {
                List<ImageCache> imageCaches = mImageCacheDao.queryBuilder()
                        .where(ImageCacheDao.Properties.Url.eq(imageUrl))
                        .build()
                        .list();
                if (!imageCaches.isEmpty()) {
                    String path = imageCaches.get(0).getPath();
                    if (!TextUtils.isEmpty(path) && FileUtil.exist(path)) {
                        imageMap.put(imageUrl, path);
                        return LocalImageProvider.constructUri(path);
                    }
                }

                if (taskArray.indexOf(imageUrl) < 0) {
                    taskArray.add(imageUrl);
                    Subscription mSubscription = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            try {
                                // 下载图片
                                File imgFile = new File(
                                        ConfigUtil.getCachePath() + File.separator + FormatUtil.getFileNameFromUrl(
                                                imageUrl));

                                if (!imgFile.exists()) {
                                    mOkHttpHelper.httpDownload(imageUrl, imgFile);
                                }
                                String path = imgFile.getAbsolutePath();
                                if (!TextUtils.isEmpty(path)) {
                                    imageMap.put(imageUrl, path);
                                    mImageCacheDao.queryBuilder()
                                            .where(ImageCacheDao.Properties.Url.eq(imageUrl))
                                            .buildDelete()
                                            .executeDeleteWithoutDetachingEntities();
                                    ImageCache cache = new ImageCache(null, imageUrl, path);
                                    mImageCacheDao.insert(cache);
                                }
                                subscriber.onNext(path);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    if (!TextUtils.isEmpty(s)) {
                                        mContentView.loadUrl("javascript:replaceImage(\""
                                                + LocalImageProvider.constructUri(s)
                                                + "\","
                                                + index
                                                + ");");
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    mContentView.loadUrl("javascript:replaceImage(\""
                                            + LocalImageProvider.constructUri(imageUrl)
                                            + "\","
                                            + index
                                            + ");");
                                }
                            });
                    mCompositeSubscription.add(mSubscription);
                }
                return "file:///android_asset/hupu_thread_default.png";
            }
        }
    }
}
