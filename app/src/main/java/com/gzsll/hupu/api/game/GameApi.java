package com.gzsll.hupu.api.game;

import android.text.TextUtils;

import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.LoginData;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.bean.PmDetailData;
import com.gzsll.hupu.bean.PmSettingData;
import com.gzsll.hupu.bean.SearchData;
import com.gzsll.hupu.bean.SendPmData;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.UserData;
import com.gzsll.hupu.components.retrofit.FastJsonConverterFactory;
import com.gzsll.hupu.components.retrofit.RequestHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
public class GameApi {
    static final String BASE_URL = "http://games.mobileapi.hupu.com/1/7.0.8/";

    private GameService mGameService;
    private RequestHelper mRequestHelper;

    public GameApi(RequestHelper mRequestHelper, OkHttpClient mOkHttpClient) {
        this.mRequestHelper = mRequestHelper;
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
        mGameService = retrofit.create(GameService.class);
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param passWord 密码
     */
    public Observable<LoginData> login(String userName, String passWord) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client", mRequestHelper.getDeviceId());
        params.put("username", userName);
        params.put("password", passWord);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.login(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }

    /**
     * 获取用户相关信息
     *
     * @param uid 用户id
     */
    public Observable<UserData> getUserInfo(String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.getUserInfo(params, mRequestHelper.getDeviceId())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取收藏帖子
     *
     * @param page 页数
     */
    public Observable<ThreadListData> getCollectList(int page) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("page", String.valueOf(page));
        String sign = mRequestHelper.getRequestSign(params);
        return mGameService.getCollectList(sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * type暂时写死，只搜索论坛
     *
     * @param key  搜索词
     * @param fid  论坛fid
     * @param page 页数
     */
    public Observable<SearchData> search(String key, String fid, int page) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("keyword", key);
        params.put("type", "posts");
        params.put("fid", fid);
        params.put("page", String.valueOf(page));
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.search(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }

    public Observable<PmData> queryPmList(String lastTime) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(lastTime)) {
            params.put("last_time", lastTime);
        }
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.queryPmList(params, mRequestHelper.getDeviceId())
                .subscribeOn(Schedulers.io());
    }

    public Observable<PmDetailData> queryPmDetail(String mid, String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(mid)) {
            params.put("pmid", mid);
        }
        params.put("from_puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.queryPmDetail(params, mRequestHelper.getDeviceId())
                .subscribeOn(Schedulers.io());
    }

    public Observable<SendPmData> pm(String content, String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(content)) {
            params.put("content", content);
        }
        params.put("receiver_puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.pm(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }

    public Observable<PmSettingData> queryPmSetting(String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("other_puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.queryPmSetting(params, mRequestHelper.getDeviceId())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseData> clearPm(String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("clear_puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.clearPm(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }

    public Observable<BaseData> blockPm(String uid, int block) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("block_puid", uid);
        params.put("is_block", String.valueOf(block));
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.blockPm(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }
}
