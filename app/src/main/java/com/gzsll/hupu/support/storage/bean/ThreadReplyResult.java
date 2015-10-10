package com.gzsll.hupu.support.storage.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sll on 2015/8/22.
 */
public class ThreadReplyResult {
    private static final String FIELD_MSG = "msg";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DATA = "data";


    @SerializedName(FIELD_MSG)
    private String mMsg;
    @SerializedName(FIELD_STATUS)
    private int mStatus;
    @SerializedName(FIELD_DATA)
    private ThreadReplyItem data;


    public ThreadReplyResult() {

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


    public ThreadReplyItem getData() {
        return data;
    }

    public void setData(ThreadReplyItem data) {
        this.data = data;
    }

}
