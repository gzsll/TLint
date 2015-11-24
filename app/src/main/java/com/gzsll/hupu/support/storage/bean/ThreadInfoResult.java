package com.gzsll.hupu.support.storage.bean;

public class ThreadInfoResult {


    private String msg;
    private int status;
    private ThreadInfoData data;


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

    public ThreadInfoData getData() {
        return data;
    }

    public void setData(ThreadInfoData data) {
        this.data = data;
    }
}