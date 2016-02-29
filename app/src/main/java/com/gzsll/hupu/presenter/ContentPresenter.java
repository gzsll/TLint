package com.gzsll.hupu.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.DBThreadInfoDao;
import com.gzsll.hupu.support.db.DBThreadReplyItemDao;
import com.gzsll.hupu.support.storage.bean.FavoriteResult;
import com.gzsll.hupu.support.storage.bean.ThreadSchemaInfo;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.view.ContentView;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2015/3/7.
 */
public class ContentPresenter extends Presenter<ContentView> {
    Logger logger = Logger.getLogger(ContentPresenter.class.getSimpleName());


    @Inject
    Gson gson;
    @Inject
    ThreadApi mThreadApi;
    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    FormatHelper mFormatHelper;
    @Inject
    NetWorkHelper mNetWorkHelper;
    @Inject
    Context mContext;
    @Inject
    DBThreadInfoDao mThreadInfoDao;
    @Inject
    DBThreadReplyItemDao mReplyDao;
    @Inject
    DbConverterHelper mDbConverterHelper;


    private String tid;
    private String fid;
    private String pid;
    public int totalPage;
    public int currentPage = 1;


    public void onThreadInfoReceive(String tid, String fid, String pid, int page) {
        this.tid = tid;
        this.fid = fid;
        this.pid = pid;
        view.showLoading();
        loadContent(page);
    }


    private void loadContent(int page) {
        mThreadApi.getThreadInfo(tid,fid,page,pid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ThreadSchemaInfo>() {
            @Override
            public void call(ThreadSchemaInfo threadSchemaInfo) {
                if (threadSchemaInfo != null) {
                    if (threadSchemaInfo.error != null) {
                        view.onError(threadSchemaInfo.error.text);
                    } else {
                        totalPage = threadSchemaInfo.pageSize;
                        view.renderContent(threadSchemaInfo.url, threadSchemaInfo.page, threadSchemaInfo.pageSize);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.onError("加载失败");
            }
        });
    }




    public void onReload() {
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
        loadContent(currentPage);
    }

    public void onPagePre() {
        currentPage--;
        if (currentPage <= 1) {
            currentPage = 1;
        }
        loadContent(currentPage);
    }

    public void onPageSelected(int page) {
        loadContent(page);
    }


    public void addArchive() {
//        mThreadApi.addSpecial(groupThreadId + "", new Callback<BaseResult>() {
//            @Override
//            public void success(BaseResult o, Response response) {
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });

    }


    public void addFavorite() {
        mThreadApi.addFavorite(tid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<FavoriteResult>() {
            @Override
            public void call(FavoriteResult favoriteResult) {
                if (favoriteResult.result != null) {
                    view.showToast(favoriteResult.result.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showToast("收藏失败");
            }
        });
    }


    public void reply(String title) {
        view.reply(title);
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
        view.showToast("复制成功");
    }


    @Override
    public void initialize() {
        view.showLoading();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }


}
