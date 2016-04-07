package com.gzsll.hupu.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.AttendStatusData;
import com.gzsll.hupu.bean.Search;
import com.gzsll.hupu.bean.SearchData;
import com.gzsll.hupu.bean.SearchResult;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.helper.NetWorkHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.ThreadListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/9.
 */
public class ThreadListPresenter extends Presenter<ThreadListView> {

    private Logger logger = Logger.getLogger(ThreadListPresenter.class.getSimpleName());
    @Inject
    Bus bus;
    @Inject
    ForumApi mForumApi;
    @Inject
    NetWorkHelper mNetWorkHelper;
    @Inject
    Context mContext;
    @Inject
    OkHttpHelper mOkHttpHelper;
    @Inject
    ToastHelper mToastHelper;
    @Inject
    OkHttpClient mOkHttpClient;
    @Inject
    GameApi mGameApi;


    @Singleton
    @Inject
    public ThreadListPresenter() {
    }


    private List<Thread> threads = new ArrayList<>();

    private String fid;
    private String lastTid = "";
    private String lastTamp = "";
    private String type;
    private int pageIndex;
    private List<String> list;
    private int loadType = TYPE_LIST;
    private String key;
    private boolean hasNextPage = true;


    private static final int TYPE_LIST = 1;
    private static final int TYPE_SEARCH = 2;

    private Subscription mSubscription;


    public void onThreadReceive(String fid, String type, List<String> list) {
        view.showLoading();
        view.onFloatingVisibility(View.VISIBLE);
        this.type = type;
        this.fid = fid;
        this.list = list;
        loadType = TYPE_LIST;
        loadThreadList("", true);
        getAttendStatus();
    }


    public void onStartSearch(String key, int page) {
        if (TextUtils.isEmpty(key)) {
            mToastHelper.showToast("搜索词不能为空");
            return;
        }
        view.showLoading();
        view.onFloatingVisibility(View.GONE);
        pageIndex = page;
        this.key = key;
        loadSearchList();
    }


    private void loadThreadList(String last, final boolean clear) {
        mSubscription = mForumApi.getThreadsList(fid, last, 20, lastTamp, type, list).map(new Func1<ThreadListData, List<Thread>>() {
            @Override
            public List<Thread> call(ThreadListData result) {
                if (result != null && result.result != null) {
                    if (clear) {
                        threads.clear();
                        view.onScrollToTop();
                    }
                    ThreadListResult data = result.result;
                    lastTamp = data.stamp;
                    hasNextPage = data.nextPage;
                    return addThreads(data.data);
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> threads) {
                if (threads != null) {
                    view.hideLoading();
                    view.renderThreads(threads);
                    view.onRefreshCompleted();
                    view.onLoadCompleted(true);
                } else {
                    loadThreadError();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadThreadError();
            }
        });
    }


    private void loadSearchList() {
        loadType = TYPE_SEARCH;
        mGameApi.search(key, fid, pageIndex).map(new Func1<SearchData, List<Thread>>() {
            @Override
            public List<Thread> call(SearchData searchData) {
                if (searchData != null) {
                    if (pageIndex == 1) {
                        threads.clear();
                        view.onScrollToTop();
                    }
                    SearchResult result = searchData.result;
                    hasNextPage = result.hasNextPage == 1;
                    for (Search search : result.data) {
                        Thread thread = new Thread();
                        thread.fid = search.fid;
                        thread.tid = search.id;
                        thread.lightReply = Integer.valueOf(search.lights);
                        thread.replies = search.replies;
                        thread.userName = search.username;
                        thread.title = search.title;
                        long time = Long.valueOf(search.addtime);
                        Date date = new Date(time);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        thread.time = format.format(date);
                        if (!contains(thread)) {
                            threads.add(thread);
                        }
                    }
                    return threads;
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> threads) {
                if (threads == null) {
                    loadThreadError();
                } else {
                    if (threads.isEmpty()) {
                        view.onEmpty();
                    } else {
                        view.hideLoading();
                        view.onRefreshCompleted();
                        view.onLoadCompleted(true);
                        view.renderThreads(threads);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadThreadError();
            }
        });
    }


    private void loadThreadError() {
        if (threads.isEmpty()) {
            view.onError("数据加载失败");
        } else {
            view.hideLoading();
            view.onRefreshCompleted();
            view.onLoadCompleted(true);
            mToastHelper.showToast("数据加载失败");
        }
    }

    private List<Thread> addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            if (!contains(thread)) {
                threads.add(thread);
            }
        }
        lastTid = threads.get(threads.size() - 1).tid;
        return threads;
    }


    private boolean contains(Thread thread) {
        boolean isContain = false;
        for (Thread thread1 : threads) {
            if (thread.tid.equals(thread1.tid)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }


    private void getAttendStatus() {
        mForumApi.getAttentionStatus(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
            @Override
            public void call(AttendStatusData attendStatusData) {
                if (attendStatusData != null && attendStatusData.status == 200) {
                    view.renderThreadInfo(attendStatusData.forumInfo);
                    view.attendStatus(attendStatusData.attendStatus == 1);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }


    public void addAttention() {
        mForumApi.addAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
            @Override
            public void call(AttendStatusData result) {
                if (result.status == 200 && result.result == 1) {
                    mToastHelper.showToast("添加关注成功");
                    view.attendStatus(result.status == 200);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("添加关注失败，请检查网络后重试");
            }
        });
    }

    public void delAttention() {
        mForumApi.delAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
            @Override
            public void call(AttendStatusData result) {
                if (result.status == 200 && result.result == 1) {
                    mToastHelper.showToast("取消关注成功");
                    view.attendStatus(result.status != 200);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("取消关注失败，请检查网络后重试");
            }
        });
    }


    public void onRefresh() {
        view.onScrollToTop();
        if (loadType == TYPE_LIST) {
            loadThreadList("", true);
        } else {
            pageIndex = 1;
            loadSearchList();
        }
    }


    public void onReload() {
        view.showLoading();
        if (loadType == TYPE_LIST) {
            loadThreadList(lastTid, false);
        } else {
            loadSearchList();
        }
    }

    public void onLoadMore() {
        if (!hasNextPage) {
            mToastHelper.showToast("没有更多了~");
            view.onLoadCompleted(false);
            return;
        }

        if (loadType == TYPE_LIST) {
            loadThreadList(lastTid, false);
        } else {
            pageIndex++;
            loadSearchList();
        }
    }


    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        threads.clear();
        lastTid = "";
        lastTamp = "";
        pageIndex = 1;
        hasNextPage = true;
    }
}
