package com.gzsll.hupu.storage.bean;

/**
 * Created by sll on 2015/6/19.
 */
public class FavoriteResult {

    /**
     * uid : 4847679
     * status : 200
     * data :
     * msg : 收藏成功
     */
    private int uid;
    private int status;
    private String data;
    private String msg;

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
}
