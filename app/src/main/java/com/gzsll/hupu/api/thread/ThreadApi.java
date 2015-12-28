package com.gzsll.hupu.api.thread;

import android.text.TextUtils;
import android.util.Log;

import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.AddReplyResult;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.FavoriteResult;
import com.gzsll.hupu.support.storage.bean.MessageAtResult;
import com.gzsll.hupu.support.storage.bean.MessageReplyResult;
import com.gzsll.hupu.support.storage.bean.MyBoardListResult;
import com.gzsll.hupu.support.storage.bean.ThreadInfoResult;
import com.gzsll.hupu.support.storage.bean.ThreadListResult;
import com.gzsll.hupu.support.storage.bean.ThreadReplyResult;
import com.gzsll.hupu.support.storage.bean.ThreadSchemaInfo;
import com.gzsll.hupu.support.storage.bean.ThreadsResult;
import com.gzsll.hupu.support.storage.bean.TopicResult;
import com.gzsll.hupu.support.storage.bean.UserResult;
import com.gzsll.hupu.support.utils.RequestHelper;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class ThreadApi {
    static final String BASE_URL = "http://bbs.mobile.hupu.com";
    static final String BASE_URL_V2 = "http://bbs.mobileapi.hupu.com/1/7.0.5";
    private ThreadService threadService;
    private ThreadService threadServiceV2;
    private RequestHelper requestHelper;
    private SettingPrefHelper mSettingPrefHelper;
    private UserStorage mUserStorage;

    public ThreadApi(final UserStorage mUserStorage, OkHttpClient okHttpClient, RequestHelper requestHelper, SettingPrefHelper mSettingPrefHelper) {
        this.mUserStorage = mUserStorage;
        this.requestHelper = requestHelper;
        this.mSettingPrefHelper = mSettingPrefHelper;
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                String token = mUserStorage.getToken();
                if (!TextUtils.isEmpty(token)) {
                    request.addHeader("cookie", "u=" + URLEncoder.encode(token) + ";");
                }
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder().setRequestInterceptor(requestInterceptor)
                .setEndpoint(BASE_URL).setClient(new OkClient(okHttpClient)).setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        threadService = restAdapter.create(ThreadService.class);

        RestAdapter restAdapterV2 = new RestAdapter.Builder().setRequestInterceptor(requestInterceptor)
                .setEndpoint(BASE_URL_V2).setClient(new OkClient(okHttpClient)).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        threadServiceV2 = restAdapterV2.create(ThreadService.class);
    }


    public void getUserInfo(String uid, Callback<UserResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        String sign = requestHelper.getRequestSign(params);
        threadService.getUserInfo(sign, params, callback);
    }

    public void getBoardList(Callback<BoardListResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getBoardList(sign, params, callback);
    }

    public void getMyBoardList(Callback<MyBoardListResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getMyBoardList(sign, params, callback);
    }

    public void getGroupThreadsList(String fid, String lastTid, int limit, String lastTamp, String type, List<String> list, Callback<ThreadListResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
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
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getGroupThreadsList(sign, params, callback);
    }

    public ThreadsResult getGroupThreadsList(String groupId, String lastId, int limit, String type, List<String> list) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupId", groupId);
        params.put("lastId", lastId);
        params.put("limit", String.valueOf(limit));
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
        String sign = requestHelper.getRequestSign(params);
        return threadService.getGroupThreadsList(sign, params);
    }

    public void addGroupAttention(String fid, Callback<AttendStatusResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.addGroupAttention(sign, params, callback);
    }

    public void delGroupAttention(String fid, Callback<AttendStatusResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.delGroupAttention(sign, params, callback);
    }

    public void getGroupAttentionStatus(String fid, Callback<AttendStatusResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getGroupAttentionStatus(sign, params, callback);
    }

    public void addSpecial(String specialId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("specialId", specialId);
        String sign = requestHelper.getRequestSign(params);
        threadService.addSpecial(sign, params, callback);
    }

    public void delSpecial(String specialId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("specialId", specialId);
        String sign = requestHelper.getRequestSign(params);
        threadService.delSpecial(sign, params, callback);
    }

    public void getGroupThreadInfo(String tid, String fid, int page, String pid, Callback<ThreadSchemaInfo> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
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
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getGroupThreadInfo(sign, params, callback);
    }

    public ThreadInfoResult getGroupThreadInfo(long groupThreadId, long lightReplyId, int page, boolean diaplayImgs) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupThreadId", groupThreadId + "");
        params.put("lightReplyId", lightReplyId + "");
        params.put("page", page + "");
        params.put("diaplayImgs", diaplayImgs ? "0" : "1");
        String sign = requestHelper.getRequestSign(params);
        return threadService.getGroupThreadInfo(sign, params);
    }

    public void addGroupThread(String title, String content, String fid, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("title", title);
        params.put("content", content);
        params.put("fid", fid);
        String sign = requestHelper.getRequestSignV2(params);
        params.put("sign", sign);
        threadServiceV2.addGroupThread(params, callback);
    }

    public void addReplyByApp(String tid, String fid, String pid, String content, Callback<AddReplyResult> callback) {
        try {

            Map<String, String> params = requestHelper.getHttpRequestMapV2();
            params.put("tid", tid);
            params.put("content", content);
            params.put("fid", fid);
            if (!TextUtils.isEmpty(pid)) {
                params.put("quotepid", pid);
                params.put("boardpw", "");
            }
            String sign = requestHelper.getRequestSignV2(params);
            params.put("sign", sign);
            Log.d("groupApi", "gson.toJson(params):" + params);
            threadServiceV2.addReplyByApp(params, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMiniReplyList(String groupThreadId, String groupReplyId, int page, Callback<ThreadReplyResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupThreadId", groupThreadId);
        params.put("groupReplyId", groupReplyId);
        params.put("page", page + "");
        params.put("limit", "20");
        String sign = requestHelper.getRequestSign(params);
        threadService.getMiniReplyList(sign, params, callback);
    }

    public void lightByApp(long groupThreadId, long groupReplyId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupThreadId", groupThreadId + "");
        params.put("groupReplyId", groupReplyId + "");
        String sign = requestHelper.getRequestSign(params);
        threadService.lightByApp(sign, params, callback);
    }

    public void addFavorite(String tid, Callback<FavoriteResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("tid", tid);
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.addFavorite(sign, params, callback);
    }

    public void delFavorite(String tid, Callback callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("tid", tid);
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.delFavorite(sign, params, callback);
    }

    public void getUserThreadList(int page, String uid, Callback<TopicResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        // params.put("username", userName);
        params.put("limit", "20");
        params.put("page", page + "");
        String sign = requestHelper.getRequestSign(params);
        threadService.getUserThreadList(sign, params, callback);
    }

    public void getUserThreadFavoriteList(int page, String uid, Callback<TopicResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        //  params.put("username", userName);
        params.put("limit", "20");
        params.put("page", page + "");
        String sign = requestHelper.getRequestSign(params);
        threadService.getUserThreadFavoriteList(sign, params, callback);
    }

    public void getMessageReply(String lastId, Callback<MessageReplyResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("lastId", lastId);
        String sign = requestHelper.getRequestSign(params);
        threadService.getMessageReply(sign, params, callback);
    }

    public void getMessageAt(String lastId, Callback<MessageAtResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("lastId", lastId);
        String sign = requestHelper.getRequestSign(params);
        threadService.getMessageAt(sign, params, callback);
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
     * @param callback
     */
    public void submitReports(String tid, String pid, String type, String content, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        if (!TextUtils.isEmpty(tid)) {
            params.put("tid", tid);
        }
        if (!TextUtils.isEmpty(pid)) {
            params.put("pid", pid);
        }
        params.put("type", type);
        params.put("content", content);
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.submitReports(sign, params, callback);
    }

    public void getRecommendThreadList(String lastTid, String lastTamp, Callback<ThreadListResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMapV2();
        params.put("lastTid", lastTid);
        params.put("isHome", "1");
        params.put("stamp", lastTamp);
        String sign = requestHelper.getRequestSignV2(params);
        threadServiceV2.getRecommendThreadList(sign, params, callback);
    }

}
