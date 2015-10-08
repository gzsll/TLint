package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;


public class ThreadInfo {

    private static final String FIELD_UID = "uid";
    private static final String FIELD_LAST_REPLY_TIME = "lastReplyTime";
    private static final String FIELD_SPECIAL = "special";
    private static final String FIELD_GROUPS = "groups";
    private static final String FIELD_SCORE = "score";
    private static final String FIELD_COLOR = "color";
    private static final String FIELD_LIGHTS = "lights";
    private static final String FIELD_SHARED_IMG = "sharedImg";
    private static final String FIELD_USER_INFO = "userInfo";
    private static final String FIELD_ATTENTION = "attention";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_ZAN = "zan";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_ID = "id";
    private static final String FIELD_DIGEST = "digest";
    private static final String FIELD_CREATE_AT_UNIXTIME = "createAtUnixtime";
    private static final String FIELD_REPLIES = "replies";
    private static final String FIELD_CREATE_AT = "createAt";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_NOTE = "note";
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_TID = "tid";


    @SerializedName(FIELD_UID)
    private int mUid;
    @SerializedName(FIELD_LAST_REPLY_TIME)
    private int mLastReplyTime;
    @SerializedName(FIELD_SPECIAL)
    private int mSpecial;
    @SerializedName(FIELD_GROUPS)
    private Group mGroup;
    @SerializedName(FIELD_SCORE)
    private double mScore;
    @SerializedName(FIELD_COLOR)
    private String mColor;
    @SerializedName(FIELD_LIGHTS)
    private int mLight;
    @SerializedName(FIELD_SHARED_IMG)
    private String mSharedImg;
    @SerializedName(FIELD_USER_INFO)
    private UserInfo mUserInfo;
    @SerializedName(FIELD_ATTENTION)
    private int mAttention;
    @SerializedName(FIELD_TYPE)
    private int mType;
    @SerializedName(FIELD_ZAN)
    private int mZan;
    @SerializedName(FIELD_USERNAME)
    private String mUsername;
    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_DIGEST)
    private int mDigest;
    @SerializedName(FIELD_CREATE_AT_UNIXTIME)
    private int mCreateAtUnixtime;
    @SerializedName(FIELD_REPLIES)
    private int mReply;
    @SerializedName(FIELD_CREATE_AT)
    private String mCreateAt;
    @SerializedName(FIELD_TITLE)
    private String mTitle;
    @SerializedName(FIELD_GROUP_ID)
    private int mGroupId;
    @SerializedName(FIELD_NOTE)
    private String mNote;
    @SerializedName(FIELD_CONTENT)
    private String mContent;
    @SerializedName(FIELD_TID)
    private int mTid;


    public ThreadInfo() {

    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public int getUid() {
        return mUid;
    }

    public void setLastReplyTime(int lastReplyTime) {
        mLastReplyTime = lastReplyTime;
    }

    public int getLastReplyTime() {
        return mLastReplyTime;
    }

    public void setSpecial(int special) {
        mSpecial = special;
    }

    public int getSpecial() {
        return mSpecial;
    }

    public void setGroup(Group group) {
        mGroup = group;
    }

    public Group getGroup() {
        return mGroup;
    }

    public void setScore(double score) {
        mScore = score;
    }

    public double getScore() {
        return mScore;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }

    public void setLight(int light) {
        mLight = light;
    }

    public int getLight() {
        return mLight;
    }

    public void setSharedImg(String sharedImg) {
        mSharedImg = sharedImg;
    }

    public String getSharedImg() {
        return mSharedImg;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setAttention(int attention) {
        mAttention = attention;
    }

    public int getAttention() {
        return mAttention;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setZan(int zan) {
        mZan = zan;
    }

    public int getZan() {
        return mZan;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setDigest(int digest) {
        mDigest = digest;
    }

    public int getDigest() {
        return mDigest;
    }

    public void setCreateAtUnixtime(int createAtUnixtime) {
        mCreateAtUnixtime = createAtUnixtime;
    }

    public int getCreateAtUnixtime() {
        return mCreateAtUnixtime;
    }

    public void setReply(int reply) {
        mReply = reply;
    }

    public int getReply() {
        return mReply;
    }

    public void setCreateAt(String createAt) {
        mCreateAt = createAt;
    }

    public String getCreateAt() {
        return mCreateAt;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public String getNote() {
        return mNote;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setTid(int tid) {
        mTid = tid;
    }

    public int getTid() {
        return mTid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreadInfo) {
            return ((ThreadInfo) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Long) mId).hashCode();
    }


}