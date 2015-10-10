package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/8/22.
 */
public class BaseResult {


    /**
     * uid : 4847679
     * status : 200
     * data :
     * msg : 发表成功
     */
    private int uid;
    private int status;
    private String data;
    private String msg;
    /**
     * notice : {"newNum":5,"newMsg":"你有5条新消息"}
     */

    private Notice notice;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }


}
