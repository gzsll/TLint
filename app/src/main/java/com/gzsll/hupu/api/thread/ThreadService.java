package com.gzsll.hupu.api.thread;

import com.gzsll.hupu.support.storage.bean.AddReplyResult;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.FavoriteResult;
import com.gzsll.hupu.support.storage.bean.MyBoardListResult;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.support.storage.bean.ThreadSchemaInfo;
import com.gzsll.hupu.support.storage.bean.UserResult;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * Created by sll on 2015/9/8 0008.
 */
public interface ThreadService {

    @GET("users/getUserBaseInfo")
    Observable<UserResult> getUserInfo(@Query("sign") String sign, @QueryMap Map<String, String> params);


    @GET("forums/getForums")
    Observable<BoardListResult> getBoardList(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("forums/getUserForumsList")
    Observable<MyBoardListResult> getMyBoardList(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("forums/getForumsInfoList")
    Observable<ThreadListResult> getGroupThreadsList(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @POST("forums/attentionForumAdd")
    @FormUrlEncoded
    Observable<AttendStatusResult> addGroupAttention(@Query("sign") String sign, @FieldMap Map<String, String> params);

    @POST("forums/attentionForumRemove")
    @FormUrlEncoded
    Observable<AttendStatusResult> delGroupAttention(@Query("sign") String sign, @FieldMap Map<String, String> params);

    @GET("forums/getForumsAttendStatus")
    Observable<AttendStatusResult> getGroupAttentionStatus(@Query("sign") String sign, @QueryMap Map<String, String> params);


    @GET("threads/getThreadsSchemaInfo")
    Observable<ThreadSchemaInfo> getThreadInfo(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @POST("threads/threadPublish")
    @FormUrlEncoded
    Observable<BaseResult> addGroupThread(@FieldMap Map<String, String> params);

    @POST("threads/threadReply")
    @FormUrlEncoded
    Observable<AddReplyResult> addReplyByApp(@FieldMap Map<String, String> params);

    @POST("threads/threadCollectAdd")
    @FormUrlEncoded
    Observable<FavoriteResult> addFavorite(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @POST("threads/threadCollectRemove")
    @FormUrlEncoded
    Observable<BoardListResult> delFavorite(@Field("sign") String sign, @FieldMap Map<String, String> params);


    @POST("threads/threadReport")
    @FormUrlEncoded
    Observable<BaseResult> submitReports(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @GET("recommend/getThreadsList")
    Observable<ThreadListResult> getRecommendThreadList(@Query("sign") String sign, @QueryMap Map<String, String> params);

}
