package com.gzsll.hupu.api.thread;

import com.gzsll.hupu.api.TypedJsonString;
import com.gzsll.hupu.support.storage.bean.AddReplyResult;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.FavoriteResult;
import com.gzsll.hupu.support.storage.bean.MessageAtResult;
import com.gzsll.hupu.support.storage.bean.MessageReplyResult;
import com.gzsll.hupu.support.storage.bean.ThreadInfoResult;
import com.gzsll.hupu.support.storage.bean.ThreadReplyResult;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.storage.bean.TopicResult;
import com.gzsll.hupu.support.storage.bean.UserResult;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by sll on 2015/9/8 0008.
 */
public interface ThreadService {

    @GET("/users/getUserBaseInfo")
    void getUserInfo(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<UserResult> callback);

    @GET("/group/getBoardList")
    void getBoardList(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BoardListResult> callback);

    @GET("/group/getGroupThreadsList")
    void getGroupThreadsList(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<ThreadsResult> callback);

    @GET("/group/addGroupAttention")
    void addGroupAttention(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BaseResult> callback);

    @GET("/group/delGroupAttention")
    void delGroupAttention(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BaseResult> callback);

    @GET("/group/addSpecial")
    void addSpecial(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BaseResult> callback);

    @GET("/group/delSpecial")
    void delSpecial(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BaseResult> callback);

    @GET("/group/getGroupThreadInfo")
    void getGroupThreadInfo(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<ThreadInfoResult> callback);

    @POST("/group/addGroupThreadByApp")
    void addGroupThread(@Body TypedJsonString json, Callback<BaseResult> callback);

    @POST("/group/addReplyByApp")
    void addReplyByApp(@Body TypedJsonString json, Callback<AddReplyResult> callback);

    @GET("/group/getMiniReplyList")
    void getMiniReplyList(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<ThreadReplyResult> callback);

    @GET("/group/lightByApp")
    void lightByApp(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BaseResult> callback);

    @GET("/group/addFavorite")
    void addFavorite(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<FavoriteResult> callback);

    @GET("/group/delFavorite")
    void delFavorite(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<BoardListResult> callback);

    @GET("/group/getUserThreadList")
    void getUserThreadList(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<TopicResult> callback);

    @GET("/group/getUserThreadFavoriteList")
    void getUserThreadFavoriteList(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<TopicResult> callback);

    @GET("/notice/getMessageReply")
    void getMessageReply(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<MessageReplyResult> callback);

    @GET("/notice/getMessageAt")
    void getMessageAt(@Query("sign") String sign, @QueryMap(encodeNames = true) Map<String, String> params, Callback<MessageAtResult> callback);

}
