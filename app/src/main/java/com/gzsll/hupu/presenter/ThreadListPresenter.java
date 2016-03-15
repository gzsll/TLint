package com.gzsll.hupu.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.AttendStatusData;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.helper.NetWorkHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.ThreadListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                if (clear) {
                    threads.clear();
                }
                if (result != null && result.result != null) {
                    ThreadListResult data = result.result;
                    lastTamp = data.stamp;
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
        String url = String
                .format("http://my.hupu.com/search?type=topic&sortby=datedesc&q=%s&fid=%s&page=%s",
                        URLEncoder.encode(key), fid, pageIndex);
        Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<Thread>>() {
            @Override
            public List<Thread> call(String s) {
                try {
                    Request request = new Request.Builder().url(s).build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return parse(new String(response.body().bytes(), "gb2312"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Thread>>() {
            @Override
            public void call(List<Thread> list) {
                if (list == null) {
                    loadThreadError();
                } else {
                    if (list.isEmpty()) {
                        view.onEmpty();
                    } else {
                        view.hideLoading();
                        view.renderThreads(list);
                    }
                }
            }
        });
    }

    private List<Thread> parse(String response) {
        Document document = Jsoup.parse(response);
        Elements eles = document.select("tbody tr");
        if (eles != null) {
            if (pageIndex == 1) {
                threads.clear();
                view.onScrollToTop();
            }
            for (int i = 0; i < eles.size(); i++) {
                Element element = eles.get(i);
                Elements attrs = element.select("td");
                if (attrs != null) {
                    Thread thread = new Thread();
                    thread.fid = fid;
                    for (int j = 0; j < attrs.size(); j++) {
                        Element td = attrs.get(j);
                        if (j == 0) {
                            Element a = td.select("a").first();
                            if (a != null) {
                                String url = a.attr("href");
                                int k = url.lastIndexOf("/") + 1;
                                String str = url.substring(k);
                                thread.tid = str.split(".html")[0];
                            }
                            thread.title = td.text();
                        }
                        if (j == 2) {
                            thread.userName = td.text();
                        }
                        if (j == 3) {
                            thread.time = td.text();
                        }
                        if (j == 4) {
                            thread.replies = td.text();
                        }

                    }
                    if ((thread.tid != null) && (!contains(thread))) {
                        threads.add(thread);
                    }
                }

            }

        }
        return threads;
    }

    private void loadThreadError() {
        if (threads.isEmpty()) {
            view.onError("数据加载失败");
        } else {
            view.hideLoading();
            view.onRefreshing(false);
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
    }
}
