package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BoardList {

    private static final String FIELD_GROUP_LIST = "groupList";
    private static final String FIELD_ID = "id";
    private static final String FIELD_ICON = "icon";
    private static final String FIELD_PID = "pid";
    private static final String FIELD_BOARD_NAME = "boardName";


    @SerializedName(FIELD_GROUP_LIST)
    private List<GroupList> mGroupLists;
    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_ICON)
    private String mIcon;
    @SerializedName(FIELD_PID)
    private int mPid;
    @SerializedName(FIELD_BOARD_NAME)
    private String mBoardName;


    public BoardList() {

    }

    public void setGroupLists(List<GroupList> groupLists) {
        mGroupLists = groupLists;
    }

    public List<GroupList> getGroupLists() {
        return mGroupLists;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setPid(int pid) {
        mPid = pid;
    }

    public int getPid() {
        return mPid;
    }

    public void setBoardName(String boardName) {
        mBoardName = boardName;
    }

    public String getBoardName() {
        return mBoardName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoardList) {
            return ((BoardList) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Long) mId).hashCode();
    }


}