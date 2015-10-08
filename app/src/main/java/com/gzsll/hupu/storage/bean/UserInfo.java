package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserInfo implements Serializable {

    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_SYNCTIME = "synctime";
    private static final String FIELD_UID = "uid";
    private static final String FIELD_ICON = "icon";
    private static final String FIELD_BANNED = "banned";
    private static final String FIELD_BADGE = "badge";
    private static final String FIELD_REPLY_NUM = "replyNum";
    private static final String FIELD_POST_NUM = "postNum";
    private static final String FIELD_GROUPS = "groups";
    private static final String FIELD_SEX = "sex";
    private static final String FIELD_FAVORITE_NUM = "favoriteNum";
    private static final String FIELD_LEVEL = "level";


    @SerializedName(FIELD_USERNAME)
    private String mUsername;
    @SerializedName(FIELD_SYNCTIME)
    private int mSynctime;
    @SerializedName(FIELD_UID)
    private int mUid;
    @SerializedName(FIELD_ICON)
    private String mIcon;
    @SerializedName(FIELD_BANNED)
    private int mBanned;
    @SerializedName(FIELD_BADGE)
    private Badge mBadge;
    @SerializedName(FIELD_REPLY_NUM)
    private int mReplyNum;
    @SerializedName(FIELD_POST_NUM)
    private int mPostNum;
    @SerializedName(FIELD_GROUPS)
    private String mGroup;
    @SerializedName(FIELD_SEX)
    private int mSex;
    @SerializedName(FIELD_FAVORITE_NUM)
    private int mFavoriteNum;
    @SerializedName(FIELD_LEVEL)
    private int mLevel;


    public UserInfo() {

    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setSynctime(int synctime) {
        mSynctime = synctime;
    }

    public int getSynctime() {
        return mSynctime;
    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public int getUid() {
        return mUid;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setBanned(int banned) {
        mBanned = banned;
    }

    public int getBanned() {
        return mBanned;
    }

    public void setBadge(Badge badge) {
        mBadge = badge;
    }

    public Badge getBadge() {
        return mBadge;
    }

    public void setReplyNum(int replyNum) {
        mReplyNum = replyNum;
    }

    public int getReplyNum() {
        return mReplyNum;
    }

    public void setPostNum(int postNum) {
        mPostNum = postNum;
    }

    public int getPostNum() {
        return mPostNum;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setSex(int sex) {
        mSex = sex;
    }

    public int getSex() {
        return mSex;
    }

    public void setFavoriteNum(int favoriteNum) {
        mFavoriteNum = favoriteNum;
    }

    public int getFavoriteNum() {
        return mFavoriteNum;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getLevel() {
        return mLevel;
    }


}