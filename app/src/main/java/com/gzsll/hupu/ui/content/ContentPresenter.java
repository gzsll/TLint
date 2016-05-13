package com.gzsll.hupu.ui.content;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.CollectData;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ShareUtils;
import com.gzsll.hupu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class ContentPresenter implements ContentContract.Presenter {

    private ForumApi mForumApi;
    private Context mContext;
    private UserStorage mUserStorage;



    private ContentContract.View mContentView;


    private String tid;
    private String fid;
    private String pid;
    private int totalPage;
    private int currentPage = 1;
    private List<String> urls = new ArrayList<>();
    private boolean isCollected;
    private String title;
    private String shareText;

    @Inject
    public ContentPresenter(ForumApi forumApi, Context context, UserStorage userStorage) {
        mForumApi = forumApi;
        mContext = context;
        mUserStorage = userStorage;
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
        mForumApi.getThreadInfo(tid, fid, page, pid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ThreadSchemaInfo>() {
            @Override
            public void call(ThreadSchemaInfo threadSchemaInfo) {
                if (threadSchemaInfo != null) {
                    if (threadSchemaInfo.error != null) {
                        mContentView.onError(threadSchemaInfo.error.text);
                    } else {
                        totalPage = threadSchemaInfo.pageSize;
                        currentPage = threadSchemaInfo.page;
                        urls = createPageList(threadSchemaInfo.url, threadSchemaInfo.page, threadSchemaInfo.pageSize);
                        shareText = threadSchemaInfo.share.weibo;
                        title = threadSchemaInfo.share.wechat_moments;
                        mContentView.renderContent(threadSchemaInfo.url, urls);
                        isCollected = threadSchemaInfo.isCollected == 1;
                        mContentView.isCollected(isCollected);
                        mContentView.hideLoading();
                    }
                } else {
                    mContentView.onError("加载失败");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mContentView.onError("加载失败");
            }
        });
    }


    private List<String> createPageList(String url, int page, int pageSize) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= pageSize; i++) {
            String newUrl = url.replace("page=" + page, "page=" + i);
            urls.add(newUrl);
        }
        return urls;
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
        mContentView.renderContent(urls.get(currentPage - 1), urls);
    }

    @Override
    public void onPagePre() {
        currentPage--;
        if (currentPage <= 1) {
            currentPage = 1;
        }
        mContentView.renderContent(urls.get(currentPage - 1), urls);
    }

    @Override
    public void onPageSelected(int page) {
        currentPage = page;
        mContentView.renderContent(urls.get(page - 1), urls);
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
            ShareUtils.share(mContext, shareText);
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
    }


    private void addCollect() {
        mForumApi.addCollect(tid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CollectData>() {
            @Override
            public void call(CollectData collectData) {
                if (collectData != null && collectData.result != null) {
                    ToastUtils.showToast(collectData.result.msg);
                    isCollected = collectData.result.status == 200;
                    mContentView.isCollected(isCollected);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtils.showToast("收藏失败");
            }
        });
    }


    private void delCollect() {
        mForumApi.delCollect(tid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CollectData>() {
            @Override
            public void call(CollectData collectData) {
                if (collectData != null && collectData.result != null) {
                    ToastUtils.showToast(collectData.result.msg);
                    isCollected = collectData.result.status != 200;
                    mContentView.isCollected(isCollected);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtils.showToast("取消收藏失败");
            }
        });
    }


    @Override
    public void attachView(@NonNull ContentContract.View view) {
        mContentView = view;
    }

    @Override
    public void detachView() {
        urls.clear();
        totalPage = 1;
        currentPage = 1;
        isCollected = false;
        shareText = "";
    }
}
