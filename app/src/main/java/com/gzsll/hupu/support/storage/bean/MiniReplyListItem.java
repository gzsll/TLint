package com.gzsll.hupu.support.storage.bean;

import java.io.Serializable;

/**
 * Created by sll on 2015/6/23.
 */
public class MiniReplyListItem implements Serializable {


    private long id;
    private long pid;
    private String formatTime;
    private int isLight;
    private long groupThreadId;
    private UserInfo userInfo;
    private int addtime;
    private String content;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public int getIsLight() {
        return isLight;
    }

    public void setIsLight(int isLight) {
        this.isLight = isLight;
    }

    public long getGroupThreadId() {
        return groupThreadId;
    }

    public void setGroupThreadId(long groupThreadId) {
        this.groupThreadId = groupThreadId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
