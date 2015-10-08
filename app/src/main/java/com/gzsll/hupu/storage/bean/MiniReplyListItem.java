package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sll on 2015/6/23.
 */
public class MiniReplyListItem implements Serializable {
    private static final String FIELD_ID = "id";
    private static final String FIELD_PID = "pid";
    private static final String FIELD_FORMAT_TIME = "formatTime";
    private static final String FIELD_IS_LIGHT = "isLight";
    private static final String FIELD_GROUP_THREAD_ID = "groupThreadId";
    private static final String FIELD_USER_INFO = "userInfo";
    private static final String FIELD_ADDTIME = "addtime";
    private static final String FIELD_CONTENT = "content";


    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_PID)
    private int mPid;
    @SerializedName(FIELD_FORMAT_TIME)
    private String mFormatTime;
    @SerializedName(FIELD_IS_LIGHT)
    private int mIsLight;
    @SerializedName(FIELD_GROUP_THREAD_ID)
    private int mGroupThreadId;
    @SerializedName(FIELD_USER_INFO)
    private UserInfo mUserInfo;
    @SerializedName(FIELD_ADDTIME)
    private int mAddtime;
    @SerializedName(FIELD_CONTENT)
    private String mContent;


    public MiniReplyListItem() {

    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setPid(int pid) {
        mPid = pid;
    }

    public int getPid() {
        return mPid;
    }


    public void setFormatTime(String formatTime) {
        mFormatTime = formatTime;
    }

    public String getFormatTime() {
        return mFormatTime;
    }


    public void setIsLight(int isLight) {
        mIsLight = isLight;
    }

    public int getIsLight() {
        return mIsLight;
    }

    public void setGroupThreadId(int groupThreadId) {
        mGroupThreadId = groupThreadId;
    }

    public int getGroupThreadId() {
        return mGroupThreadId;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setAddtime(int addtime) {
        mAddtime = addtime;
    }

    public int getAddtime() {
        return mAddtime;
    }


    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreadReplyItem) {
            return ((ThreadReplyItem) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Long) mId).hashCode();
    }
}
