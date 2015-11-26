package com.gzsll.hupu.support.storage.bean;

import java.util.List;


public class ThreadsData {


    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<GroupThread> getGroupThreads() {
        return groupThreads;
    }

    public void setGroupThreads(List<GroupThread> groupThreads) {
        this.groupThreads = groupThreads;
    }

    private String lastId;
    private Info info;
    private List<GroupThread> groupThreads;





}