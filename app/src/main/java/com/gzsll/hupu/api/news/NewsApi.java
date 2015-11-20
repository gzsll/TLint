package com.gzsll.hupu.api.news;

import com.gzsll.hupu.support.storage.bean.NewsResult;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by sll on 2015/10/10.
 */
public class NewsApi {
    static final String BASE_URL = "http://games.mobileapi.hupu.com/3/7.0.4";

    private NewsService mNewsService;

    public NewsApi(OkHttpClient okHttpClient) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setClient(new OkClient(okHttpClient)).setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        mNewsService = restAdapter.create(NewsService.class);
    }

    public void getNbaNews(String nid, Callback<NewsResult> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("client", UUID.randomUUID().toString());
        params.put("night", "0");
        params.put("num", "20");
        params.put("direc", "next");
        params.put("preload", "0");
        params.put("nid", nid);
        mNewsService.getNbaNews(params, callback);
    }
}
