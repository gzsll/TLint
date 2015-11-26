package com.gzsll.hupu.support.storage.bean;

public class BoardListResult {


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BoardListData getData() {
        return data;
    }

    public void setData(BoardListData data) {
        this.data = data;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    private String msg;
    private int status;
    private BoardListData data;
    private Notice notice;



}