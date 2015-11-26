package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/8/22.
 */
public class ThreadReplyResult {


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

    public ThreadReplyItem getData() {
        return data;
    }

    public void setData(ThreadReplyItem data) {
        this.data = data;
    }

    private String msg;
    private int status;
    private ThreadReplyItem data;




}
