package com.gzsll.hupu.api.thread;

import android.text.TextUtils;
import android.util.Log;

import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.AddReplyResult;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.FavoriteResult;
import com.gzsll.hupu.support.storage.bean.MyBoardListResult;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.support.storage.bean.ThreadSchemaInfo;
import com.gzsll.hupu.support.storage.bean.UserResult;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Created by sll on 2015/9/8 0008.
 */
public class ThreadApi {


    static final String BASE_URL = "http://bbs.mobileapi.hupu.com/1/7.0.5/";

    private ThreadService mThreadService;
    private RequestHelper mRequestHelper;
    private SettingPrefHelper mSettingPrefHelper;
    private UserStorage mUserStorage;

    public ThreadApi(RequestHelper mRequestHelper, SettingPrefHelper mSettingPrefHelper, UserStorage mUserStorage, OkHttpClient mOkHttpClient) {
        this.mRequestHelper = mRequestHelper;
        this.mSettingPrefHelper = mSettingPrefHelper;
        this.mUserStorage = mUserStorage;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mThreadService = retrofit.create(ThreadService.class);
    }

    public Observable<UserResult> getUserInfo(String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("uid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getUserInfo(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<BoardListResult> getBoardList() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getBoardList(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<MyBoardListResult> getMyBoardList() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getMyBoardList(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<ThreadListResult> getGroupThreadsList(String fid, String lastTid, int limit, String lastTamp, String type, List<String> list) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("lastTid", lastTid);
        params.put("limit", String.valueOf(limit));
        params.put("isHome", "1");
        params.put("stamp", lastTamp);
        params.put("password", "0");
        if (list == null) {
            params.put("special", "0");
            params.put("type", type);
        } else {
            JSONArray jSONArray = new JSONArray();
            for (String str : list) {
                jSONArray.put(str);
            }
            params.put("gids", jSONArray.toString());
        }
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getGroupThreadsList(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<AttendStatusResult> addGroupAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.addGroupAttention(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<AttendStatusResult> delGroupAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.delGroupAttention(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<AttendStatusResult> getGroupAttentionStatus(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getGroupAttentionStatus(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<ThreadSchemaInfo> getThreadInfo(String tid, String fid, int page, String pid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(tid)) {
            params.put("tid", tid);
        }
        if (!TextUtils.isEmpty(fid)) {
            params.put("fid", fid);
        }
        params.put("page", page + "");
        if (!TextUtils.isEmpty(pid)) {
            params.put("pid", pid);
        }
        params.put("nopic", mSettingPrefHelper.getLoadPic() ? "0" : "1");
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getThreadInfo(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<BaseResult> addGroupThread(String title, String content, String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("title", title);
        params.put("content", content);
        params.put("fid", fid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mThreadService.addGroupThread(params).subscribeOn(Schedulers.io());
    }

    public Observable<AddReplyResult> addReplyByApp(String tid, String fid, String pid, String content) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        params.put("content", content);
        params.put("fid", fid);
        if (!TextUtils.isEmpty(pid)) {
            params.put("quotepid", pid);
            params.put("boardpw", "");
        }
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        Log.d("groupApi", "gson.toJson(params):" + params);
        return mThreadService.addReplyByApp(params).subscribeOn(Schedulers.io());

    }


    public Observable<FavoriteResult> addFavorite(String tid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.addFavorite(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable delFavorite(String tid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.delFavorite(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * (1, "广告或垃圾内容");
     * (2, "色情暴露内容");
     * (3, "政治敏感话题");
     * (4, "人身攻击等恶意行为");
     *
     * @param tid
     * @param pid
     * @param type
     * @param content
     */
    public Observable<BaseResult> submitReports(String tid, String pid, String type, String content) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(tid)) {
            params.put("tid", tid);
        }
        if (!TextUtils.isEmpty(pid)) {
            params.put("pid", pid);
        }
        params.put("type", type);
        params.put("content", content);
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.submitReports(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<ThreadListResult> getRecommendThreadList(String lastTid, String lastTamp) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("lastTid", lastTid);
        params.put("isHome", "1");
        params.put("stamp", lastTamp);
        String sign = mRequestHelper.getRequestSign(params);
        return mThreadService.getRecommendThreadList(sign, params).subscribeOn(Schedulers.io());
    }

}
