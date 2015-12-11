package com.gzsll.hupu.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.DBThreadInfoDao;
import com.gzsll.hupu.support.db.DBThreadReplyItemDao;
import com.gzsll.hupu.support.storage.bean.ThreadSchemaInfo;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.view.ContentView;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * Created by sll on 2015/3/7.
 */
public class ContentPresenter extends Presenter<ContentView> {
    Logger logger = Logger.getLogger(ContentPresenter.class.getSimpleName());


    @Inject
    Gson gson;
    @Inject
    ThreadApi threadApi;
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


    private Handler mHandler = new Handler(Looper.getMainLooper());


    private String tid;
    private String fid;
    public int totalPage;
    public int currentPage = 1;


    public void onThreadInfoReceive(String tid, String fid, int page) {
        this.tid = tid;
        this.fid = fid;
        view.showLoading();
        loadContent(page);
    }


    private void loadContent(int page) {
        currentPage = page;
        if (mNetWorkHelper.isFast() || page > 1) {
            loadFromNet(page);
        } else {
            loadFromDb();
        }
    }


    private void loadFromNet(int page) {
        threadApi.getGroupThreadInfo(tid, fid, page, "0", new retrofit.Callback<ThreadSchemaInfo>() {
            @Override
            public void success(ThreadSchemaInfo threadInfoResult, retrofit.client.Response response) {
                if (threadInfoResult != null) {
                    view.renderContent(threadInfoResult.url, threadInfoResult.page, threadInfoResult.pageSize);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                view.onError("加载失败");
            }
        });
    }


    private void loadFromDb() {
//
//        BackgroundExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<DBThreadInfo> threadInfoList = mThreadInfoDao.queryBuilder().where(DBThreadInfoDao.Properties.ServerId.eq(groupThreadId)).list();
//                if (!threadInfoList.isEmpty()) {
//                    threadInfo = mDbConverterHelper.convertDbThreadInfo(threadInfoList.get(0));
//                    Map map = gson.fromJson(gson.toJson(threadInfo), new TypeToken<Map<Object, Object>>() {
//                    }.getType());
//                    renderContent(map);
//                }
//
//                List<ThreadReplyItems> replyItems = new ArrayList<>();
//                List<DBThreadReplyItem> hotReplies = mReplyDao.queryBuilder().where(DBThreadReplyItemDao.Properties.GroupThreadId.eq(groupThreadId), DBThreadReplyItemDao.Properties.IsHot.eq(true)).orderDesc(DBThreadReplyItemDao.Properties.Lights).list();
//                if (!hotReplies.isEmpty()) {
//                    ThreadReplyItems replyItem = new ThreadReplyItems();
//                    replyItem.setmLists(mDbConverterHelper.convertDbThreadReplyItems(hotReplies));
//                    replyItem.setName("这些回帖亮了");
//                    replyItems.add(replyItem);
//                }
//
//                List<DBThreadReplyItem> replies = mReplyDao.queryBuilder().where(DBThreadReplyItemDao.Properties.GroupThreadId.eq(groupThreadId), DBThreadReplyItemDao.Properties.IsHot.eq(false)).orderAsc(DBThreadReplyItemDao.Properties.Floor).list();
//                if (!replies.isEmpty()) {
//                    ThreadReplyItems replyItem = new ThreadReplyItems();
//                    replyItem.setmLists(mDbConverterHelper.convertDbThreadReplyItems(replies));
//                    replyItem.setName("全部回帖");
//                    currentPage = 1;
//                    totalPage = (threadInfo.getReplies() / 20) + 1;
//                    replyItem.setCurrentPage(currentPage);
//                    replyItem.setTotalPage(totalPage);
//                    replyItems.add(replyItem);
//                }
//                renderReplies(currentPage, totalPage, replyItems);
//
//            }
//        });
    }

//    private void renderContent(final Map map) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                view.hideLoading();
//                view.renderContent(map);
//            }
//        });
//    }
//
//    private void renderReplies(final int currentPage, final int totalPage, final List<ThreadReplyItems> replyItems) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                view.renderReplies(currentPage, totalPage, replyItems);
//            }
//        });
//    }


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

    public void onPageSelected(int page) {
        loadContent(page);
    }


    public void addArchive() {
//        threadApi.addSpecial(groupThreadId + "", new Callback<BaseResult>() {
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
//        threadApi.addFavorite(groupThreadId, new Callback<FavoriteResult>() {
//            @Override
//            public void success(FavoriteResult favoriteResult, Response response) {
//                view.showToast(favoriteResult.getMsg());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                view.showToast("收藏失败");
//            }
//        });
    }


    public void reply() {
        view.reply("");
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
