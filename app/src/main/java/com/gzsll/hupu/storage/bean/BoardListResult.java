package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;


public class BoardListResult {

    private static final String FIELD_MSG = "msg";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DATA = "data";


    @SerializedName(FIELD_MSG)
    private String mMsg;
    @SerializedName(FIELD_STATUS)
    private int mStatus;
    @SerializedName(FIELD_DATA)
    private BoardListData mDatum;
    private Notice notice;


    public BoardListResult() {

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

    public void setData(BoardListData datum) {
        mDatum = datum;
    }

    public BoardListData getData() {
        return mDatum;
    }


    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}