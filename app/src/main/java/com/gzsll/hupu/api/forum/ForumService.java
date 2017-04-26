package com.gzsll.hupu.api.forum;

import com.gzsll.hupu.bean.AttendStatusData;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.CollectData;
import com.gzsll.hupu.bean.ForumsData;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.bean.MyForumsData;
import com.gzsll.hupu.bean.PermissionData;
import com.gzsll.hupu.bean.PostData;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.bean.UploadData;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sll on 2016/3/8.
 * 论坛相关api
 */
public interface ForumService {

  @GET("forums/getForums") Observable<ForumsData> getForums(@Query("sign") String sign,
      @QueryMap Map<String, String> params);

  @GET("forums/getUserForumsList") Observable<MyForumsData> getMyForums(@Query("sign") String sign,
      @QueryMap Map<String, String> params);

  @GET("forums/getForumsInfoList") Observable<ThreadListData> getThreadsList(
      @Query("sign") String sign, @QueryMap Map<String, String> params);

  @POST("forums/attentionForumAdd") @FormUrlEncoded Observable<AttendStatusData> addAttention(
      @Query("sign") String sign, @FieldMap Map<String, String> params);

  @POST("forums/attentionForumRemove") @FormUrlEncoded Observable<AttendStatusData> delAttention(
      @Query("sign") String sign, @FieldMap Map<String, String> params);

  @GET("forums/getForumsAttendStatus") Observable<AttendStatusData> getAttentionStatus(
      @Query("sign") String sign, @QueryMap Map<String, String> params);

  @GET("threads/getThreadsSchemaInfo") Observable<ThreadSchemaInfo> getThreadInfo(
      @Query("sign") String sign, @QueryMap Map<String, String> params);

  @POST("threads/threadPublish")
  @FormUrlEncoded
  Observable<PostData> addThread(
          @FieldMap Map<String, String> params);

  @POST("threads/threadReply")
  @FormUrlEncoded
  Observable<PostData> addReplyByApp(
          @FieldMap Map<String, String> params);

  @POST("threads/threadCollectAdd") @FormUrlEncoded Observable<CollectData> addCollect(
      @Field("sign") String sign, @FieldMap Map<String, String> params);

  @POST("threads/threadCollectRemove") @FormUrlEncoded Observable<CollectData> delCollect(
      @Field("sign") String sign, @FieldMap Map<String, String> params);

  @POST("threads/threadReport") @FormUrlEncoded Observable<BaseData> submitReports(
      @Field("sign") String sign, @FieldMap Map<String, String> params);

  @GET("recommend/getThreadsList") Observable<ThreadListData> getRecommendThreadList(
      @Query("sign") String sign, @QueryMap Map<String, String> params);

  @GET("user/getUserMessageList") Observable<MessageData> getMessageList(@Query("sign") String sign,
      @QueryMap Map<String, String> params);

  @POST("user/delUserMessage") @FormUrlEncoded Observable<BaseData> delMessage(
      @Field("sign") String sign, @FieldMap Map<String, String> params);

  @POST("img/Imgup") @Multipart Observable<UploadData> upload(@Part MultipartBody.Part file,
      @PartMap Map<String, RequestBody> params);

  @GET("permission/check") Observable<PermissionData> checkPermission(@Query("sign") String sign,
      @QueryMap Map<String, String> params);
}
