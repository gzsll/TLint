package com.gzsll.hupu.presenter;

import android.os.Handler;
import android.os.Looper;

import com.gzsll.hupu.otto.NotificationEvent;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Notification;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.view.NotificationView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
public class NotificationPresenter extends Presenter<NotificationView> {


    private List<Notification> notifications = new ArrayList<>();
    private static final String IGNORE_URL = "http://my.hupu.com/include/remind_act.php?jsoncallback=jsonp";
    private static final String NOTIFICATION_URL = "http://my.hupu.com/notifications/json/";
    private static final String PM_URL = "http://my.hupu.com/mymsg.php";


    @Inject
    UserStorage mUserStorage;
    @Inject
    OkHttpHelper mOkHttpHelper;
    @Inject
    Bus mBus;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void loadNotification() {
        if (view != null) {
            view.showLoading();
        }
        String token = URLEncoder.encode(mUserStorage.getToken());
        Request request = new Request.Builder().url(NOTIFICATION_URL).header("Host", "my.hupu.com").header("Accept-Encoding", "utf-8")
                .header("Cookie",
                        "u=" + token + ";").build();
        mOkHttpHelper.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    parser(response.body().string());
                }
            }
        });
    }


    private void parser(String response) {
        try {
            notifications.clear();
            JSONObject json = new JSONObject(response);
            if (json.has("msg")) {
                int msg = json.getInt("msg");
                if (msg > 0) {
                    Notification item = new Notification();
                    item.msg = "短消息(" + msg + "条未读)";
                    item.type = Notification.TYPE_MESSAGE;
                    item.href = PM_URL;
                    item.nid = "0";
                    notifications.add(item);
                }
            }
            if (json.has("notificationsinfo")) {
                JSONArray array = json.getJSONArray("notificationsinfo");
                for (int i = 0; i < array.length(); i++) {
                    Notification item = new Notification();
                    JSONObject obj = array.getJSONObject(i);
                    String id = obj.getString("id");
                    String title = obj.getString("title");
                    Document doc = Jsoup.parse(title);
                    Element info = doc.select("a").first();
                    String msg = doc.body().text();
                    String href = info.attr("href");
                    item.nid = id;
                    item.msg = msg;
                    item.href = href;
                    item.type = Notification.TYPE_MESSAGE;
                    // parser href
                    int lastIndexOfSlash = href.lastIndexOf("/");
                    String sub = href.substring(lastIndexOfSlash + 1);
                    String pid = "0";
                    int indexOfPound = sub.indexOf("#");

                    if (indexOfPound != -1) {
                        pid = sub.substring(indexOfPound + 1);
                        item.pid = pid;
                        int indexOfDot = sub.indexOf(".");
                        sub = sub.substring(0, indexOfDot);
                        String[] elements = sub.split("-");
                        if (elements != null && elements.length == 2) {
                            item.tid = elements[0];
                            item.page = elements[1];
                        } else {
                            item.tid = sub;
                            item.page = "1";
                        }
                        item.type = Notification.TYPE_THREAD;
                    }

                    notifications.add(item);
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (view != null) {
                        if (notifications.isEmpty()) {
                            view.onEmpty();
                        } else {
                            view.renderList(notifications);
                            view.hideLoading();
                        }
                    }
                    mBus.post(new NotificationEvent(notifications.size()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ignore(String nid) {
        String token = URLEncoder.encode(mUserStorage.getToken());
        String url = IGNORE_URL + timestamp() + "&id=" + nid;
        Request request = new Request.Builder().url(url)
                .header("Cookie",
                        "u=" + token + ";").build();
        mOkHttpHelper.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    public String timestamp() {
        Date date = new Date();
        long time = date.getTime();
        return "" + time;
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
