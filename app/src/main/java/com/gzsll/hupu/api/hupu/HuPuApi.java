package com.gzsll.hupu.api.hupu;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzsll.hupu.api.TypedJsonString;
import com.gzsll.hupu.storage.UserStorage;
import com.gzsll.hupu.storage.bean.AddReplyResult;
import com.gzsll.hupu.storage.bean.BaseResult;
import com.gzsll.hupu.storage.bean.BoardListResult;
import com.gzsll.hupu.storage.bean.FavoriteResult;
import com.gzsll.hupu.storage.bean.MessageAtResult;
import com.gzsll.hupu.storage.bean.MessageReplyResult;
import com.gzsll.hupu.storage.bean.ThreadInfoResult;
import com.gzsll.hupu.storage.bean.ThreadReplyResult;
import com.gzsll.hupu.storage.bean.ThreadsResult;
import com.gzsll.hupu.storage.bean.TopicResult;
import com.gzsll.hupu.storage.bean.UserResult;
import com.gzsll.hupu.utils.RequestHelper;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

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
public class HuPuApi {
    static final String BASE_URL = "http://bbs.mobile.hupu.com";

    private HuPuService huPuService;
    private RequestHelper requestHelper;
    private Gson gson;
    private UserStorage mUserStorage;

    public HuPuApi(final UserStorage mUserStorage, OkHttpClient okHttpClient, RequestHelper requestHelper, Gson gson) {
        this.mUserStorage = mUserStorage;
        this.requestHelper = requestHelper;
        this.gson = gson;
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
                .setEndpoint(BASE_URL).setClient(new OkClient(okHttpClient)).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        huPuService = restAdapter.create(HuPuService.class);
    }


    public void getUserInfo(String uid, Callback<UserResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        String sign = requestHelper.getRequestSign(params);
        huPuService.getUserInfo(sign, params, callback);
    }

    public void getBoardList(Callback<BoardListResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        String sign = requestHelper.getRequestSign(params);
        huPuService.getBoardList(sign, params, callback);
    }

    public void getGroupThreadsList(String groupId, String lastId, String type, List<String> list, Callback<ThreadsResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupId", groupId);
        params.put("lastId", lastId);
        params.put("limit", "20");
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
        huPuService.getGroupThreadsList(sign, params, callback);
    }

    public void addGroupAttention(String groupId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupId", groupId);
        params.put("uid", mUserStorage.getUid());
        String sign = requestHelper.getRequestSign(params);
        huPuService.addGroupAttention(sign, params, callback);
    }

    public void delGroupAttention(String groupId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupId", groupId);
        params.put("uid", mUserStorage.getUid());
        String sign = requestHelper.getRequestSign(params);
        huPuService.delGroupAttention(sign, params, callback);
    }

    public void addSpecial(String specialId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("specialId", specialId);
        String sign = requestHelper.getRequestSign(params);
        huPuService.addSpecial(sign, params, callback);
    }

    public void delSpecial(String specialId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("specialId", specialId);
        String sign = requestHelper.getRequestSign(params);
        huPuService.delSpecial(sign, params, callback);
    }

    public void getGroupThreadInfo(long groupThreadId, long lightReplyId, int page, boolean diaplayImgs, Callback<ThreadInfoResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupThreadId", groupThreadId + "");
        params.put("lightReplyId", lightReplyId + "");
        params.put("page", page + "");
        params.put("diaplayImgs", diaplayImgs ? "0" : "1");
        String sign = requestHelper.getRequestSign(params);
        huPuService.getGroupThreadInfo(sign, params, callback);
    }

    public void addGroupThread(String title, String content, String groupId, List<String> list, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("title", title);
        params.put("content", content);
        params.put("groupId", groupId);
        if (list != null && !list.isEmpty()) {
            JSONArray jSONArray = new JSONArray();
            for (String put : list) {
                jSONArray.put(put);
            }
            params.put("imgs", jSONArray.toString());
        }
        String sign = requestHelper.getRequestSign(params);
        params.put("sign", sign);
        huPuService.addGroupThread(new TypedJsonString(gson.toJson(params)), callback);
    }

    public void addReplyByApp(String groupThreadId, String groupReplyId, String quoteId, String content, List<String> list, Callback<AddReplyResult> callback) {
        try {

            Map<String, String> params = requestHelper.getHttpRequestMap();
            params.put("groupThreadId", groupThreadId + "");
            params.put("content", content);
            params.put("groupReplyId", groupReplyId + "");
            if (Long.valueOf(quoteId) > 0) {
                params.put("quoteId", quoteId + "");
            }

            if (Long.valueOf(groupReplyId) > 0) {
                params.put("replyId", groupReplyId + "");
            }
            JSONObject jsonObject = new JSONObject(gson.toJson(params));
            if (list != null && !list.isEmpty()) {
                JSONArray jSONArray = new JSONArray();
                for (String put : list) {
                    jSONArray.put(put);
                }
                params.put("imgs", jSONArray.toString());
                jsonObject.putOpt("imgs", jSONArray);
            }
            String sign = requestHelper.getRequestSign(params);
            jsonObject.putOpt("sign", sign);
            Log.d("groupApi", "gson.toJson(params):" + jsonObject.toString());
            huPuService.addReplyByApp(new TypedJsonString(jsonObject.toString()), callback);
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
        huPuService.getMiniReplyList(sign, params, callback);
    }

    public void lightByApp(long groupThreadId, long groupReplyId, Callback<BaseResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("groupThreadId", groupThreadId + "");
        params.put("groupReplyId", groupReplyId + "");
        String sign = requestHelper.getRequestSign(params);
        huPuService.lightByApp(sign, params, callback);
    }

    public void addFavorite(long tid, Callback<FavoriteResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("tid", tid + "");
        String sign = requestHelper.getRequestSign(params);
        huPuService.addFavorite(sign, params, callback);
    }

    public void delFavorite(long tid, Callback callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("tid", tid + "");
        String sign = requestHelper.getRequestSign(params);
        huPuService.delFavorite(sign, params, callback);
    }

    public void getUserThreadList(int page, String uid, Callback<TopicResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        // params.put("username", userName);
        params.put("limit", "20");
        params.put("page", page + "");
        String sign = requestHelper.getRequestSign(params);
        huPuService.getUserThreadList(sign, params, callback);
    }

    public void getUserThreadFavoriteList(int page, String uid, Callback<TopicResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("uid", uid);
        //  params.put("username", userName);
        params.put("limit", "20");
        params.put("page", page + "");
        String sign = requestHelper.getRequestSign(params);
        huPuService.getUserThreadFavoriteList(sign, params, callback);
    }

    public void getMessageReply(String lastId, Callback<MessageReplyResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("lastId", lastId);
        String sign = requestHelper.getRequestSign(params);
        huPuService.getMessageReply(sign, params, callback);
    }

    public void getMessageAt(String lastId, Callback<MessageAtResult> callback) {
        Map<String, String> params = requestHelper.getHttpRequestMap();
        params.put("lastId", lastId);
        String sign = requestHelper.getRequestSign(params);
        huPuService.getMessageAt(sign, params, callback);
    }

}
