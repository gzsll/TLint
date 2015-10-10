package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/8/13.
 */
public class UserResult {
    private int status;
    private String msg;
    private UserInfo data;
    private int uid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
