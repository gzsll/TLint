package com.gzsll.hupu.support.storage.bean;

public class ThreadsResult {


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

    public ThreadsData getData() {
        return data;
    }

    public void setData(ThreadsData data) {
        this.data = data;
    }

    private String msg;
    private int status;
    private ThreadsData data;



}