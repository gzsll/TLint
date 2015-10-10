package com.gzsll.hupu.support.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ThreadsData {

    private static final String FIELD_LAST_ID = "lastId";
    private static final String FIELD_INFO = "info";
    private static final String FIELD_GROUP_THREADS = "groupThreads";


    @SerializedName(FIELD_LAST_ID)
    private String mLastId;
    @SerializedName(FIELD_INFO)
    private Info mInfo;
    @SerializedName(FIELD_GROUP_THREADS)
    private List<GroupThread> mGroupThreads;


    public ThreadsData() {

    }

    public void setLastId(String lastId) {
        mLastId = lastId;
    }

    public String getLastId() {
        return mLastId;
    }

    public void setInfo(Info info) {
        mInfo = info;
    }

    public Info getInfo() {
        return mInfo;
    }

    public void setGroupThreads(List<GroupThread> groupThreads) {
        mGroupThreads = groupThreads;
    }

    public List<GroupThread> getGroupThreads() {
        return mGroupThreads;
    }


}