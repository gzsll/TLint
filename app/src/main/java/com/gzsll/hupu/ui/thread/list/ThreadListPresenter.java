package com.gzsll.hupu.ui.thread.list;

import android.support.annotation.NonNull;
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
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.injector.PerActivity;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class ThreadListPresenter implements ThreadListContract.Presenter {

    private Logger logger = Logger.getLogger(ThreadListPresenter.class.getSimpleName());

    private String fid;
    private ForumApi mForumApi;
    private GameApi mGameApi;
    private UserStorage mUserStorage;


    private ThreadListContract.View mThreadListView;
    private List<Thread> threads = new ArrayList<>();

    private String lastTid = "";
    private String lastTamp = "";
    private String type;
    private int pageIndex;
    private int loadType = TYPE_LIST;
    private String key;
    private boolean hasNextPage = true;
    private boolean isAttention = false;

    private static final int TYPE_LIST = 1;
    private static final int TYPE_SEARCH = 2;

    private Subscription mSubscription;

    @Inject
    public ThreadListPresenter(String fid, ForumApi forumApi, GameApi gameApi, UserStorage userStorage) {
        this.fid = fid;
        mForumApi = forumApi;
        mGameApi = gameApi;
        mUserStorage = userStorage;
    }


    @Override
    public void onThreadReceive(String type) {
        mThreadListView.showLoading();
        mThreadListView.onFloatingVisibility(View.VISIBLE);
        this.type = type;
        loadType = TYPE_LIST;
        loadThreadList("", true);
        getAttendStatus();
    }

    @Override
    public void onStartSearch(String key, int page) {
        if (TextUtils.isEmpty(key)) {
            mThreadListView.showToast("搜索词不能为空");
            return;
        }
        mThreadListView.showLoading();
        mThreadListView.onFloatingVisibility(View.GONE);
        pageIndex = page;
        this.key = key;
        loadSearchList();
    }


    private void loadThreadList(String last, final boolean clear) {
        mSubscription = mForumApi.getThreadsList(fid, last, 20, lastTamp, type, null).map(new Func1<ThreadListData, List<Thread>>() {
            @Override
            public List<Thread> call(ThreadListData result) {
                if (result != null && result.result != null) {
                    if (clear) {
                        threads.clear();
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
                    mThreadListView.hideLoading();
                    mThreadListView.renderThreads(threads);
                    mThreadListView.onRefreshCompleted();
                    mThreadListView.onLoadCompleted(hasNextPage);
                    if (clear) {
                        mThreadListView.onScrollToTop();
                    }
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
                        mThreadListView.onEmpty();
                    } else {
                        mThreadListView.hideLoading();
                        mThreadListView.renderThreads(threads);
                        mThreadListView.onRefreshCompleted();
                        mThreadListView.onLoadCompleted(hasNextPage);
                        if (pageIndex == 1) {
                            mThreadListView.onScrollToTop();
                        }
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
            mThreadListView.onError("数据加载失败");
        } else {
            mThreadListView.hideLoading();
            mThreadListView.onRefreshCompleted();
            mThreadListView.onLoadCompleted(true);
            mThreadListView.showToast("数据加载失败");
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
                    mThreadListView.renderThreadInfo(attendStatusData.forumInfo);
                    isAttention = attendStatusData.attendStatus == 1;
                    mThreadListView.attendStatus(isAttention);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }


    @Override
    public void onAttentionClick() {
        if (isLogin()) {
            if (isAttention) {
                delAttention();
            } else {
                addAttention();
            }
        }
    }

    @Override
    public void onPostClick() {
        if (isLogin()) {
            mThreadListView.showPostThreadUi(fid);
        }
    }


    private boolean isLogin() {
        if (!mUserStorage.isLogin()) {
            mThreadListView.showLoginUi();
            return false;
        }
        return true;
    }


    private void addAttention() {
        mForumApi.addAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
            @Override
            public void call(AttendStatusData result) {
                if (result.status == 200 && result.result == 1) {
                    mThreadListView.showToast("添加关注成功");
                    isAttention = result.status == 200;
                    mThreadListView.attendStatus(isAttention);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mThreadListView.showToast("添加关注失败，请检查网络后重试");
            }
        });
    }

    private void delAttention() {
        mForumApi.delAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusData>() {
            @Override
            public void call(AttendStatusData result) {
                if (result.status == 200 && result.result == 1) {
                    mThreadListView.showToast("取消关注成功");
                    isAttention = result.status != 200;
                    mThreadListView.attendStatus(isAttention);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mThreadListView.showToast("取消关注失败，请检查网络后重试");
            }
        });
    }

    @Override
    public void onRefresh() {
        mThreadListView.onScrollToTop();
        if (loadType == TYPE_LIST) {
            loadThreadList("", true);
        } else {
            pageIndex = 1;
            loadSearchList();
        }
    }

    @Override
    public void onReload() {
        mThreadListView.showLoading();
        if (loadType == TYPE_LIST) {
            loadThreadList(lastTid, false);
        } else {
            loadSearchList();
        }
    }

    @Override
    public void onLoadMore() {
        if (!hasNextPage) {
            mThreadListView.showToast("没有更多了~");
            mThreadListView.onLoadCompleted(false);
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
    public void attachView(@NonNull ThreadListContract.View view) {
        mThreadListView = view;
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
        mThreadListView = null;
    }
}
