package com.gzsll.hupu.support.storage.bean;

import java.util.List;


public class BoardList {


    private List<GroupList> groupList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    private long id;
    private String icon;
    private int pid;
    private String boardName;


    public List<GroupList> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupList> groupList) {
        this.groupList = groupList;
    }
}