package com.gzsll.hupu.ui.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.CollectData;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.helper.ShareHelper;
import com.gzsll.hupu.helper.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
public class ContentPresenter implements ContentContract.Presenter {

    @Inject
    ForumApi mForumApi;
    @Inject
    Context mContext;
    @Inject
    ToastHelper mToastHelper;
    @Inject
    ShareHelper mShareHelper;

    @Inject
    @Singleton
    public ContentPresenter() {
    }


    private ContentContract.View mContentView;
    private String tid;
    private String fid;
    private String pid;
    public int totalPage;
    public int currentPage = 1;
    private List<String> urls = new ArrayList<>();


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
                        mContentView.renderContent(threadSchemaInfo.url, urls);
                        mContentView.isCollected(threadSchemaInfo.isCollected == 1);
                        mContentView.renderShare(threadSchemaInfo.share.weibo, threadSchemaInfo.share.url);
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


    public void onReload() {
        mContentView.showLoading();
        loadContent(currentPage);
    }

    public void onRefresh() {
        loadContent(currentPage);
    }


    public void onPageNext() {
        currentPage++;
        if (currentPage >= totalPage) {
            currentPage = totalPage;
        }
        mContentView.renderContent(urls.get(currentPage - 1), urls);
    }

    public void onPagePre() {
        currentPage--;
        if (currentPage <= 1) {
            currentPage = 1;
        }
        mContentView.renderContent(urls.get(currentPage - 1), urls);
    }

    public void onPageSelected(int page) {
        currentPage = page;
        mContentView.renderContent(urls.get(page - 1), urls);
    }


    public void updatePage(int page) {
        currentPage = page;
    }

    public void addCollect() {
        mForumApi.addCollect(tid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CollectData>() {
            @Override
            public void call(CollectData collectData) {
                if (collectData != null && collectData.result != null) {
                    mToastHelper.showToast(collectData.result.msg);
                    mContentView.isCollected(collectData.result.status == 200);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("收藏失败");
            }
        });
    }


    public void delCollect() {
        mForumApi.delCollect(tid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CollectData>() {
            @Override
            public void call(CollectData collectData) {
                if (collectData != null && collectData.result != null) {
                    mToastHelper.showToast(collectData.result.msg);
                    mContentView.isCollected(collectData.result.status != 200);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("取消收藏失败");
            }
        });
    }


    public void copy(String stripped) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(stripped);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("content", stripped);
            clipboard.setPrimaryClip(clip);
        }
        mToastHelper.showToast("复制成功");
    }


    public void onShare(String text) {
        mShareHelper.share(text);
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
    }
}
