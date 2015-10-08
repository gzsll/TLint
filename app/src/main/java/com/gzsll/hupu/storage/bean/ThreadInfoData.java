package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;


public class ThreadInfoData {

    private static final String FIELD_FAVORITE = "favorite";
    private static final String FIELD_THREAD_REPLY = "threadReply";
    private static final String FIELD_THREAD_INFO = "threadInfo";
    private static final String FIELD_PAGE = "page";
    private static final String FIELD_THREAD_HOT_REPLY = "threadHotReply";


    @SerializedName(FIELD_FAVORITE)
    private int mFavorite;
    @SerializedName(FIELD_THREAD_REPLY)
    private ThreadReply mThreadReply;
    @SerializedName(FIELD_THREAD_INFO)
    private ThreadInfo mThreadInfo;
    @SerializedName(FIELD_PAGE)
    private int mPage;
    @SerializedName(FIELD_THREAD_HOT_REPLY)
    private ThreadHotReply mThreadHotReply;


    public ThreadInfoData() {

    }

    public void setFavorite(int favorite) {
        mFavorite = favorite;
    }

    public int getFavorite() {
        return mFavorite;
    }

    public void setThreadReply(ThreadReply threadReply) {
        mThreadReply = threadReply;
    }

    public ThreadReply getThreadReply() {
        return mThreadReply;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        mThreadInfo = threadInfo;
    }

    public ThreadInfo getThreadInfo() {
        return mThreadInfo;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPage() {
        return mPage;
    }

    public void setThreadHotReply(ThreadHotReply threadHotReply) {
        mThreadHotReply = threadHotReply;
    }

    public ThreadHotReply getThreadHotReply() {
        return mThreadHotReply;
    }


}