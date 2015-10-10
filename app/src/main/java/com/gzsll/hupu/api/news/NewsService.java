package com.gzsll.hupu.api.news;

import com.gzsll.hupu.support.storage.bean.NewsResult;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by sll on 2015/10/10.
 */
public interface NewsService {
    @GET("/nba/getNews")
    void getNbaNews(@QueryMap(encodeNames = true) Map<String, String> params, Callback<NewsResult> callback);
}
