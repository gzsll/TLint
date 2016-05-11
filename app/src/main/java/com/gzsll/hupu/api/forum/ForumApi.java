package com.gzsll.hupu.api.forum;

import android.text.TextUtils;
import android.util.Log;

import com.gzsll.hupu.bean.AttendStatusData;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.CollectData;
import com.gzsll.hupu.bean.ForumsData;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.bean.MyForumsData;
import com.gzsll.hupu.bean.PermissionData;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.bean.ThreadSchemaInfo;
import com.gzsll.hupu.bean.UploadData;
import com.gzsll.hupu.components.retrofit.FastJsonConverterFactory;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/8.
 */
public class ForumApi {

    static final String BASE_URL = "http://bbs.mobileapi.hupu.com/1/7.0.8/";

    private ForumService mForumService;
    private RequestHelper mRequestHelper;
    private SettingPrefHelper mSettingPrefHelper;
    private UserStorage mUserStorage;

    public ForumApi(RequestHelper mRequestHelper, SettingPrefHelper mSettingPrefHelper, UserStorage mUserStorage, OkHttpClient mOkHttpClient) {
        this.mRequestHelper = mRequestHelper;
        this.mSettingPrefHelper = mSettingPrefHelper;
        this.mUserStorage = mUserStorage;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mForumService = retrofit.create(ForumService.class);
    }

    /**
     * 获取所有论坛列表
     *
     * @return
     */
    public Observable<ForumsData> getForums() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getForums(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 获取用户收藏的论坛列表
     *
     * @return
     */
    public Observable<MyForumsData> getMyForums() {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getMyForums(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 获取论坛帖子列表
     *
     * @param fid      论坛id，通过getForums接口获取
     * @param lastTid  最后一篇帖子的id
     * @param limit    分页大小
     * @param lastTamp 时间戳
     * @param type     加载类型  1 按发帖时间排序  2 按回帖时间排序
     * @param list     未知 ，暂时没使用
     * @return
     */
    public Observable<ThreadListData> getThreadsList(String fid, String lastTid, int limit, String lastTamp, String type, List<String> list) {
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


    /**
     * 添加关注
     *
     * @param fid 论坛id
     * @return
     */
    public Observable<AttendStatusData> addAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.addAttention(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 取消关注
     *
     * @param fid 论坛id
     * @return
     */
    public Observable<AttendStatusData> delAttention(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.delAttention(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 获取论坛关注状态
     *
     * @param fid 论坛id
     * @return
     */
    public Observable<AttendStatusData> getAttentionStatus(String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("fid", fid);
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getAttentionStatus(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 获取帖子详情
     *
     * @param tid  帖子id
     * @param fid  论坛id
     * @param page 页数
     * @param pid  回复id
     * @return
     */
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


    /**
     * 发新帖
     *
     * @param title   标题
     * @param content 内容
     * @param fid     论坛id
     * @return
     */
    public Observable<BaseData> addThread(String title, String content, String fid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("title", title);
        params.put("content", content);
        params.put("fid", fid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mForumService.addThread(params).subscribeOn(Schedulers.io());
    }


    /**
     * 评论或者回复
     *
     * @param tid     帖子id
     * @param fid     论坛id
     * @param pid     回复id（评论时为空，回复某条回复的为回复的id）
     * @param content 内容
     * @return
     */
    public Observable<BaseData> addReplyByApp(String tid, String fid, String pid, String content) {
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

    /**
     * 收藏帖子
     *
     * @param tid 帖子id
     * @return
     */
    public Observable<CollectData> addCollect(String tid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("tid", tid);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.addCollect(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 取消收藏帖子
     *
     * @param tid 帖子id
     * @return
     */
    public Observable<CollectData> delCollect(String tid) {
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
    public Observable<BaseData> submitReports(String tid, String pid, String type, String content) {
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

    /**
     * 获取推荐帖子列表
     *
     * @param lastTid
     * @param lastTamp
     * @return
     */
    public Observable<ThreadListData> getRecommendThreadList(String lastTid, String lastTamp) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("lastTid", lastTid);
        params.put("isHome", "1");
        params.put("stamp", lastTamp);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getRecommendThreadList(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 获取论坛消息列表
     *
     * @param lastTid 上一条消息id
     * @param page    页数
     * @return
     */
    public Observable<MessageData> getMessageList(String lastTid, int page) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("messageID", lastTid);
        params.put("page", String.valueOf(page));
        params.put("uid", mUserStorage.getUid());
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.getMessageList(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 阅读某条消息
     *
     * @param id 消息id
     * @return
     */
    public Observable<BaseData> delMessage(String id) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.delMessage(sign, params).subscribeOn(Schedulers.io());
    }


    /**
     * 上传图片
     *
     * @param path 图片地址
     * @return
     */
    public Observable<UploadData> upload(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentType(path)), file);
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        Map<String, RequestBody> requestBody = new HashMap<>();
        for (String key : params.keySet()) {
            String value = params.get(key);
            requestBody.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        return mForumService.upload(body, requestBody);
    }


    private String getContentType(String str) {
        if (str == null) {
            return null;
        }
        if (str.endsWith(".jpe") || str.endsWith(".JPE") || str.endsWith(".JPEG") || str.endsWith(".jpeg") || str.endsWith(".jpg") || str.endsWith(".JPG")) {
            return "image/jpeg";
        }
        if (str.endsWith(".png") || str.endsWith(".PNG")) {
            return "image/png";
        }
        if (str.endsWith(".gif")) {
            return "image/gif";
        }
        return null;
    }


    /**
     * 检查权限
     *
     * @param fid    论坛id
     * @param tid    帖子id
     * @param action threadPublish  threadReply
     * @return
     */
    public Observable<PermissionData> checkPermission(String fid, String tid, String action) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        if (!TextUtils.isEmpty(fid)) {
            params.put("fid", fid);
        }
        if (!TextUtils.isEmpty(tid)) {
            params.put("tid", tid);
        }
        if (!TextUtils.isEmpty(action)) {
            params.put("action", action);
        }
        String sign = mRequestHelper.getRequestSign(params);
        return mForumService.checkPermission(sign, params).subscribeOn(Schedulers.io());
    }


}
