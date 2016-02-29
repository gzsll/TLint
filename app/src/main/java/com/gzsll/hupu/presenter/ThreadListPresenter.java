package com.gzsll.hupu.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.DBGroupThreadDao;
import com.gzsll.hupu.support.db.DBGroupsDao;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.support.utils.DbConverterHelper;
import com.gzsll.hupu.support.utils.NetWorkHelper;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.view.ThreadListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by sll on 2015/3/4.
 */
public class ThreadListPresenter extends Presenter<ThreadListView> {

    private Logger logger = Logger.getLogger(ThreadListPresenter.class.getSimpleName());
    @Inject
    Bus bus;
    @Inject
    ThreadApi mThreadApi;
    @Inject
    NetWorkHelper mNetWorkHelper;
    @Inject
    Context mContext;
    @Inject
    DBGroupThreadDao mThreadDao;
    @Inject
    DBGroupsDao mGroupsDao;
    @Inject
    DbConverterHelper mDbConverterHelper;
    @Inject
    OkHttpHelper mOkHttpHelper;


    private Handler handler = new Handler(Looper.getMainLooper());


    private List<Thread> threads = new ArrayList<Thread>();


    private String fid;
    private String lastTid;
    private String lastTamp = "";
    private String type;
    private int pageIndex;
    private List<String> list;
    private int loadType = TYPE_LIST;
    private boolean clear;
    private String key;


    private static final int TYPE_LIST = 1;
    private static final int TYPE_SEARCH = 2;


    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        //for onPause()
    }

    @Override
    public void destroy() {

    }

    public void onThreadReceive(String fid, String type, List<String> list) {
        view.showLoading();
        view.onFloatingVisibility(View.VISIBLE);
        this.type = type;
        this.fid = fid;
        this.list = list;
        clear = true;
        loadType = TYPE_LIST;
        loadThreadList("");
        getAttendStatus();
    }


    public void onStartSearch(String key, int page) {
        if (TextUtils.isEmpty(key)) {
            view.showToast("搜索词不能为空");
            return;
        }
        view.showLoading();
        view.onFloatingVisibility(View.GONE);
        pageIndex = page;
        this.key = key;
        clear = true;
        loadSearchList();
    }


    private void loadSearchList() {
        loadType = TYPE_SEARCH;
        String url = String
                .format("http://my.hupu.com/search?type=topic&sortby=datedesc&q=%s&fid=%s&page=%s",
                        URLEncoder.encode(key), fid, pageIndex);
        Request request = new Request.Builder().url(url).build();
        mOkHttpHelper.enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError("搜索失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    parse(new String(response.body().bytes(), "gb2312"));
                } else {
                    searchError();
                }
            }
        });
    }


    private void getAttendStatus() {
        mThreadApi.getGroupAttentionStatus(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusResult>() {
            @Override
            public void call(AttendStatusResult attendStatusResult) {
                if (attendStatusResult.status == 200) {
                    view.renderThreadInfo(attendStatusResult.forumInfo);
                    view.attendStatus(attendStatusResult.attendStatus);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }


    private void loadThreadList(String last) {
        mThreadApi.getGroupThreadsList(fid, last, 20, lastTamp, type, list).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ThreadListResult>() {
            @Override
            public void call(ThreadListResult result) {
                if (clear) {
                    threads.clear();
                    view.onScrollToTop();
                }
                if (result.result != null) {
                    lastTamp = result.result.stamp;
                    addThreads(result.result.data);
                    view.renderThreads(threads);
                    view.hideLoading();
                } else {
                    if (threads.isEmpty()) {
                        view.onError("数据加载失败");
                    } else {
                        view.onRefreshing(false);
                        view.showToast("数据加载失败");
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.onError("数据加载失败");
            }
        });
    }


    private void addThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            if (!contains(thread)) {
                threads.add(thread);
            }
        }
        lastTid = threads.get(threads.size() - 1).tid;
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


    private void parse(String response) {
        Document document = Jsoup.parse(response);
        Elements eles = document.select("tbody tr");
        if (eles != null) {
            if (clear) {
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
                    if ((thread != null) && (thread.tid != null) && (!contains(thread))) {
                        threads.add(thread);
                    }
                }

            }
            if (!threads.isEmpty()) {
                view.renderThreads(threads);
                view.hideLoading();
            } else {
                view.onEmpty();
            }
        } else {
            searchError();
        }
    }

    private void searchError() {
        if (threads.isEmpty()) {
            view.onError("搜索失败，请重试");
        } else {
            view.onRefreshing(false);
            view.showToast("数据加载失败");
        }
    }


    public void onRefresh() {
        view.onScrollToTop();
        clear = true;
        if (loadType == TYPE_LIST) {
            loadThreadList("");
        } else {
            pageIndex = 1;
            loadSearchList();
        }
    }

    public void onReload() {
        clear = false;
        if (loadType == TYPE_LIST) {
            loadThreadList(lastTid);
        } else {
            loadSearchList();
        }
    }


    public void onLoadMore() {
        clear = false;
        if (loadType == TYPE_LIST) {
            loadThreadList(lastTid);
        } else {
            pageIndex++;
            loadSearchList();
        }
    }


    public void addAttention() {
        mThreadApi.addGroupAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusResult>() {
            @Override
            public void call(AttendStatusResult result) {
                if (result.status == 200 && result.result == 1) {
                    view.showToast("添加关注成功");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showToast("添加关注失败，请检查网络后重试");
            }
        });
    }

    public void delAttention() {
        mThreadApi.delGroupAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusResult>() {
            @Override
            public void call(AttendStatusResult result) {
                if (result.status == 200 && result.result == 1) {
                    view.showToast("取消关注成功");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showToast("取消关注失败，请检查网络后重试");
            }
        });
    }


}
