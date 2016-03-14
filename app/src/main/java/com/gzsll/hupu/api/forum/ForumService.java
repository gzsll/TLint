package com.gzsll.hupu.api.forum;

import com.gzsll.hupu.bean.AttendStatusResult;
import com.gzsll.hupu.bean.BaseResult;
import com.gzsll.hupu.bean.CollectResult;
import com.gzsll.hupu.bean.ForumsResult;
import com.gzsll.hupu.bean.MessageResult;
import com.gzsll.hupu.bean.MyForumsResult;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.bean.ThreadSchemaInfo;

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
 * Created by sll on 2016/3/8.
 * 论坛相关api
 */
public interface ForumService {


    @GET("forums/getForums")
    Observable<ForumsResult> getForums(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("forums/getUserForumsList")
    Observable<MyForumsResult> getMyForums(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("forums/getForumsInfoList")
    Observable<ThreadListResult> getThreadsList(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @POST("forums/attentionForumAdd")
    @FormUrlEncoded
    Observable<AttendStatusResult> addAttention(@Query("sign") String sign, @FieldMap Map<String, String> params);

    @POST("forums/attentionForumRemove")
    @FormUrlEncoded
    Observable<AttendStatusResult> delAttention(@Query("sign") String sign, @FieldMap Map<String, String> params);

    @GET("forums/getForumsAttendStatus")
    Observable<AttendStatusResult> getAttentionStatus(@Query("sign") String sign, @QueryMap Map<String, String> params);


    @GET("threads/getThreadsSchemaInfo")
    Observable<ThreadSchemaInfo> getThreadInfo(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @POST("threads/threadPublish")
    @FormUrlEncoded
    Observable<BaseResult> addThread(@FieldMap Map<String, String> params);

    @POST("threads/threadReply")
    @FormUrlEncoded
    Observable<BaseResult> addReplyByApp(@FieldMap Map<String, String> params);

    @POST("threads/threadCollectAdd")
    @FormUrlEncoded
    Observable<CollectResult> addCollect(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @POST("threads/threadCollectRemove")
    @FormUrlEncoded
    Observable<CollectResult> delCollect(@Field("sign") String sign, @FieldMap Map<String, String> params);


    @POST("threads/threadReport")
    @FormUrlEncoded
    Observable<BaseResult> submitReports(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @GET("recommend/getThreadsList")
    Observable<ThreadListResult> getRecommendThreadList(@Query("sign") String sign, @QueryMap Map<String, String> params);

    @POST("user/getUserMessageList")
    @FormUrlEncoded
    Observable<MessageResult> getMessageList(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @POST("user/delUserMessage")
    @FormUrlEncoded
    Observable<BaseResult> delMessage(@Field("sign") String sign, @FieldMap Map<String, String> params);


}
