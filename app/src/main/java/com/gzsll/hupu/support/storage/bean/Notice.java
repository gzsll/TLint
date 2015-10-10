package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/9/6 0006.
 */
public class Notice {
    /**
     * newNum : 2
     * newMsg : 你有2条新消息
     */

    private int newNum;
    private String newMsg;

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    public void setNewMsg(String newMsg) {
        this.newMsg = newMsg;
    }

    public int getNewNum() {
        return newNum;
    }

    public String getNewMsg() {
        return newMsg;
    }
}
