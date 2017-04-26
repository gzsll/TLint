package com.gzsll.hupu.ui.content;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.CollectData;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.otto.UpdateContentPageEvent;
import com.gzsll.hupu.util.ShareUtil;
import com.gzsll.hupu.util.ToastUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentPresenter implements ContentContract.Presenter {

    private ForumApi mForumApi;
    private Context mContext;
    private UserStorage mUserStorage;
    private Bus mBus;

    private ContentContract.View mContentView;
    private Subscription mSubscription;

    private String tid;
    private String fid;
    private String pid;
    private int totalPage = 1;
    private int currentPage = 1;
    private boolean isCollected;
    private String title;
    private String shareText;
    private boolean isSuccess;

    @Inject
    public ContentPresenter(ForumApi forumApi, Context context, UserStorage userStorage, Bus mBus) {
        mForumApi = forumApi;
        mContext = context;
        mUserStorage = userStorage;
        this.mBus = mBus;
    }

    @Override
    public void onThreadInfoReceive(String tid, String fid, String pid, int page) {
        this.tid = tid;
        this.fid = fid;
        this.pid = pid;
        mContentView.showLoading();
        loadContent(page);
    }

    private void loadContent(int page) {
        mSubscription = mForumApi.getThreadSchemaInfo(tid, fid, page, pid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThreadSchemaInfo>() {
                    @Override
                    public void call(ThreadSchemaInfo threadSchemaInfo) {
                        if (threadSchemaInfo != null) {
                            if (threadSchemaInfo.error != null) {
                                mContentView.onError(threadSchemaInfo.error.text);
                            } else {
                                totalPage = threadSchemaInfo.pageSize;
                                currentPage = threadSchemaInfo.page;
                                shareText = threadSchemaInfo.share.weibo;
                                title = threadSchemaInfo.share.wechat_moments;
                                mContentView.renderContent(currentPage, totalPage);
                                isCollected = threadSchemaInfo.isCollected == 1;
                                mContentView.isCollected(isCollected);
                                mContentView.hideLoading();
                                isSuccess = true;
                            }
                        } else {
                            mContentView.onError("加载失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mContentView.renderContent(currentPage, totalPage);
                        mContentView.hideLoading();
                    }
                });
    }

    @Override
    public void onReload() {
        mContentView.showLoading();
        loadContent(currentPage);
    }

    @Override
    public void onRefresh() {
        loadContent(currentPage);
    }

    @Override
    public void onPageNext() {
        currentPage++;
        if (currentPage >= totalPage) {
            currentPage = totalPage;
        }
        mContentView.setCurrentItem(currentPage - 1);
    }

    @Override
    public void onPagePre() {
        currentPage--;
        if (currentPage <= 1) {
            currentPage = 1;
        }
        mContentView.setCurrentItem(currentPage - 1);
    }

    @Override
    public void onPageSelected(int page) {
        currentPage = page;
        mContentView.setCurrentItem(currentPage - 1);
    }

    @Override
    public void onCommendClick() {
        if (isLogin()) {
            mContentView.showPostUi(title);
        }
        mContentView.onToggleFloatingMenu();
    }

    private boolean isLogin() {
        if (!mUserStorage.isLogin()) {
            mContentView.showLoginUi();
            return false;
        }
        return true;
    }

    @Override
    public void onShareClick() {
        if (!TextUtils.isEmpty(shareText)) {
            ShareUtil.share(mContext, shareText);
        }
        mContentView.onToggleFloatingMenu();
    }

    @Override
    public void onReportClick() {
        if (isLogin()) {
            mContentView.showReportUi();
        }
        mContentView.onToggleFloatingMenu();
    }

    @Override
    public void onCollectClick() {
        if (isLogin()) {
            if (isCollected) {
                delCollect();
            } else {
                addCollect();
            }
        }
        mContentView.onToggleFloatingMenu();
    }

    public void updatePage(int page) {
        currentPage = page;
        mContentView.onUpdatePager(currentPage, totalPage);
    }

    private void addCollect() {
        mForumApi.addCollect(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CollectData>() {
                    @Override
                    public void call(CollectData collectData) {
                        if (collectData != null && collectData.result != null) {
                            ToastUtil.showToast(collectData.result.msg);
                            isCollected = collectData.result.status == 200;
                            mContentView.isCollected(isCollected);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast("收藏失败");
                    }
                });
    }

    private void delCollect() {
        mForumApi.delCollect(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CollectData>() {
                    @Override
                    public void call(CollectData collectData) {
                        if (collectData != null && collectData.result != null) {
                            ToastUtil.showToast(collectData.result.msg);
                            isCollected = collectData.result.status != 200;
                            mContentView.isCollected(isCollected);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast("取消收藏失败");
                    }
                });
    }

    @Override
    public void attachView(@NonNull ContentContract.View view) {
        mContentView = view;
        mBus.register(this);
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mBus.unregister(this);
        mContentView = null;
    }

    @Subscribe
    public void onUpdateContentPageEvent(UpdateContentPageEvent event) {
        if (!isSuccess) {
            currentPage = event.getPage();
            totalPage = event.getTotalPage();
            mContentView.renderContent(currentPage, totalPage);
            isSuccess = true;
        }
    }
}
