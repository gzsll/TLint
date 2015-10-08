package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;


public class ThreadsResult {

    private static final String FIELD_MSG = "msg";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DATA = "data";


    @SerializedName(FIELD_MSG)
    private String mMsg;
    @SerializedName(FIELD_STATUS)
    private int mStatus;
    @SerializedName(FIELD_DATA)
    private ThreadsData data;


    public ThreadsResult() {

    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getStatus() {
        return mStatus;
    }


    public ThreadsData getData() {
        return data;
    }

    public void setData(ThreadsData data) {
        this.data = data;
    }

    private Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }
}