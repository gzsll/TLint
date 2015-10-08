package com.gzsll.hupu.storage.bean;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageAt {
    private UserInfo userInfo;
    private String formatTime;
    private int id;
    private int type;
    private ThreadReplyItem reply;


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ThreadReplyItem getReply() {
        return reply;
    }

    public void setReply(ThreadReplyItem reply) {
        this.reply = reply;
    }
}
