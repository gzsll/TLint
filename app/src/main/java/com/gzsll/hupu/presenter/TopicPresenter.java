package com.gzsll.hupu.presenter;

import android.os.Handler;
import android.os.Looper;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Topic;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.view.TopicView;
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
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sll on 2015/5/28.
 */
public class TopicPresenter extends Presenter<TopicView> {
    Logger logger = Logger.getLogger(TopicPresenter.class.getSimpleName());


    @Inject
    ThreadApi mThreadApi;
    @Inject
    Bus bus;
    @Inject
    UserStorage mUserStorage;
    @Inject
    OkHttpHelper mOkHttpHelper;

    private static final String TOPIC_URL = "http://my.hupu.com/%s/topic-list-%d";
    private static final String FAV_URL = "http://my.hupu.com/%s/topic-fav-%d";
    private static final int BOARD_INDEX = 1;
    private static final int REPLIES_INDEX = 3;
    private static final int TITLE_INDEX = 0;


    private List<Topic> topics = new ArrayList<Topic>();
    private int type;
    private String uid;
    private int page;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void load(int type, String uid, final int page) {
        this.page = page;
        String url;
        if (type == Constants.NAV_TOPIC_LIST) {
            url = String.format(Locale.getDefault(), TOPIC_URL, uid,
                    page);
        } else {
            url = String
                    .format(Locale.getDefault(), FAV_URL, uid, page);
        }
        String cookie = mUserStorage.getCookie();
        Request request = new Request.Builder().url(url).header("Cookie", "u=" + URLEncoder.encode(cookie)).build();
        mOkHttpHelper.enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    parser(new String(response.body().bytes(), "gb2312"), page == 1);
                }
            }
        });
    }

    private void loadError() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (topics.isEmpty()) {
                    view.onError("数据加载失败，请重试");
                } else {
                    view.showToast("数据加载失败");
                }
            }
        });
    }


    private void parser(String response, boolean clear) {
        try {
            Document doc = Jsoup.parse(response);
            Elements eles = doc.select("tbody tr");
            if (eles != null) {
                if (clear) {
                    topics.clear();
                }
                for (int i = 0; i < eles.size(); i++) {
                    Element element = eles.get(i);
                    Elements attrs = element.select("td");
                    if (attrs != null) {
                        Topic topic = new Topic();
                        for (int j = 0; j < attrs.size(); j++) {
                            Element td = attrs.get(j);
                            if (j == TITLE_INDEX) {
                                Element a = td.select("a").first();
                                if (a != null) {
                                    String url = a.attr("href");
                                    String tid = sub(".com/", ".html",
                                            url);
                                    topic.tid = tid;
                                }
                                topic.title = td.text();
                            } else if (j == BOARD_INDEX) {
                                topic.boardName = td.text();

                            } else if (j == REPLIES_INDEX) {
                                topic.replies = td.text();
                            }
                        }
                        if (topic != null && topic.tid != null && !contains(topic)) {
                            topics.add(topic);
                        }

                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!topics.isEmpty()) {
                            view.renderList(topics);
                            view.hideLoading();
                        } else {
                            view.onEmpty();
                        }
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
            loadError();
        }
    }

    public String sub(String begin, String end, String src) throws Exception {
        int indexBegin = src.indexOf(begin);
        if (indexBegin != -1) {
            String sub = src.substring(indexBegin + begin.length());
            if (end == null) {
                return sub;
            }
            int indexEnd = sub.indexOf(end);
            if (indexEnd != -1) {
                return sub.substring(0, indexEnd);
            }
        }
        return null;
    }


    private boolean contains(Topic item) {
        for (Topic topic : topics) {
            if (item != null && item.tid != null && topic.tid != null) {
                if (topic.tid.equals(item.tid)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void onTopicReceive(int type, String uid) {
        view.showLoading();
        this.type = type;
        this.uid = uid;
        load(type, uid, 1);
    }


    public void onRefresh() {
        view.onScrollToTop();
        load(type, uid, 1);

    }

    public void onLoadMore() {
        page = page + 1;
        load(type, uid, page);
    }

    public void onReload() {
        load(type, uid, page);
    }


    @Override
    public void initialize() {

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
