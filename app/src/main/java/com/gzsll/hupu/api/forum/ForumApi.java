package com.gzsll.hupu.api.forum;

import android.text.TextUtils;
import android.util.Log;

import com.gzsll.hupu.bean.AttendStatusResult;
import com.gzsll.hupu.bean.BaseResult;
import com.gzsll.hupu.bean.CollectResult;
import com.gzsll.hupu.bean.ForumsResult;
import com.gzsll.hupu.bean.MessageResult;
import com.gzsll.hupu.bean.MyForumsResult;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.components.retrofit.GsonConverterFactory;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/8.
 */
public class ForumApi {

    static final String BASE_URL = "http://bbs.mobileapi.hupu.com/1/7.0.7/";

    private ForumService mForumService;
    private RequestHelper mRequestHelper;
    private SettingPrefHelper mSettingPrefHelper;
    private UserStorage mUserStorage;

    public ForumApi(RequestHelper mRequestHelper, SettingPrefHelper mSettingPrefHelper, UserStorage mUserStorage, OkHttpClient mOkHttpClient) {
        this.mRequestHelper = mRequestHelper;
        this.mSettingPrefHelper = mSettingPrefHelper;
        this.mUserStorage = mUserStorage;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mForumService = retrofit.create(ForumService.class);
    }


    public Observable<ForumsResult> getForums() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getForums(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<MyForumsResult> getMyForums() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getMyForums(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<ThreadListResult> getThreadsList(String fid, String lastTid, int limit, String lastTamp, String type, List<String> list) {
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
        return mForumService.getThreadsList(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<AttendStatusResult> addAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.addAttention(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<AttendStatusResult> delAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.delAttention(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<AttendStatusResult> getAttentionStatus(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getAttentionStatus(sign, params).subscribeOn(Schedulers.io());
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
        return mForumService.getThreadInfo(sign, params).subscribeOn(Schedulers.io());
    }


    public Observable<BaseResult> addThread(String title, String content, String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("title", title);
        params.put("content", content);
        params.put("fid", fid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mForumService.addThread(params).subscribeOn(Schedulers.io());
    }

    public Observable<BaseResult> addReplyByApp(String tid, String fid, String pid, String content) {
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
        return mForumService.addReplyByApp(params).subscribeOn(Schedulers.io());

    }


    public Observable<CollectResult> addCollect(String tid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.addCollect(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<CollectResult> delCollect(String tid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.delCollect(sign, params).subscribeOn(Schedulers.io());
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
        return mForumService.submitReports(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<ThreadListResult> getRecommendThreadList(String lastTid, String lastTamp) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("lastTid", lastTid);
        params.put("isHome", "1");
        params.put("stamp", lastTamp);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getRecommendThreadList(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<MessageResult> getMessageList(String lastTid, int page) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("messageID", lastTid);
        params.put("page", String.valueOf(page));
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getMessageList(sign, params).subscribeOn(Schedulers.io());
    }

    public Observable<BaseResult> delMessage(String id) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.delMessage(sign, params).subscribeOn(Schedulers.io());
    }


}
