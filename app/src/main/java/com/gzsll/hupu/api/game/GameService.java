package com.gzsll.hupu.api.game;

import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.LoginData;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.bean.PmDetailData;
import com.gzsll.hupu.bean.PmSettingData;
import com.gzsll.hupu.bean.SearchData;
import com.gzsll.hupu.bean.SendPmData;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.UserData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sll on 2016/3/10.
 */
public interface GameService {

    @FormUrlEncoded
    @POST("user/loginUsernameEmail")
    Observable<LoginData> login(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @FormUrlEncoded
    @POST("user/page")
    Observable<UserData> getUserInfo(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @GET("collect/getThreadsCollectList")
    Observable<ThreadListData> getCollectList(
            @Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("search/list")
    Observable<SearchData> search(@QueryMap Map<String, String> params,
                                  @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/list")
    Observable<PmData> queryPmList(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/detail")
    Observable<PmDetailData> queryPmDetail(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/send")
    Observable<SendPmData> pm(@FieldMap Map<String, String> params,
                              @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/setting")
    Observable<PmSettingData> queryPmSetting(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/clear")
    Observable<BaseData> clearPm(
            @FieldMap Map<String, String> params, @Query("client") String client);

    @FormUrlEncoded
    @POST("pm/block")
    Observable<BaseData> blockPm(
            @FieldMap Map<String, String> params, @Query("client") String client);
}
