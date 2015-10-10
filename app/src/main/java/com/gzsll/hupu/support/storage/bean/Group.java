package com.gzsll.hupu.support.storage.bean;

import com.google.gson.annotations.SerializedName;


public class Group {

    private static final String FIELD_UID = "uid";
    private static final String FIELD_ORDER_BY = "orderBy";
    private static final String FIELD_CATEGORY_ID = "categoryId";
    private static final String FIELD_GROUP_AVATOR = "groupAvator";
    private static final String FIELD_COLOR = "color";
    private static final String FIELD_COLOR_STYLE = "colorStyle";
    private static final String FIELD_CATEGORY_NAME = "categoryName";
    private static final String FIELD_GROUP_ARTICLE = "groupArticle";
    private static final String FIELD_GROUP_BOARD_ID = "groupBoardId";
    private static final String FIELD_GROUP_NAME = "groupName";
    private static final String FIELD_ID = "id";
    private static final String FIELD_PID = "pid";
    private static final String FIELD_GROUP_NOTE = "groupNote";
    private static final String FIELD_GROUP_MEMBER = "groupMember";
    private static final String FIELD_CREATE_AT = "createAt";
    private static final String FIELD_GROUP_COVER = "groupCover";


    @SerializedName(FIELD_UID)
    private int mUid;
    @SerializedName(FIELD_ORDER_BY)
    private int mOrderBy;
    @SerializedName(FIELD_CATEGORY_ID)
    private int mCategoryId;
    @SerializedName(FIELD_GROUP_AVATOR)
    private String mGroupAvator;
    @SerializedName(FIELD_COLOR)
    private String mColor;
    @SerializedName(FIELD_COLOR_STYLE)
    private int mColorStyle;
    @SerializedName(FIELD_CATEGORY_NAME)
    private String mCategoryName;
    @SerializedName(FIELD_GROUP_ARTICLE)
    private int mGroupArticle;
    @SerializedName(FIELD_GROUP_BOARD_ID)
    private int mGroupBoardId;
    @SerializedName(FIELD_GROUP_NAME)
    private String mGroupName;
    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_PID)
    private int mPid;
    @SerializedName(FIELD_GROUP_NOTE)
    private String mGroupNote;
    @SerializedName(FIELD_GROUP_MEMBER)
    private int mGroupMember;
    @SerializedName(FIELD_CREATE_AT)
    private int mCreateAt;
    @SerializedName(FIELD_GROUP_COVER)
    private String mGroupCover;


    public Group() {

    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public int getUid() {
        return mUid;
    }

    public void setOrderBy(int orderBy) {
        mOrderBy = orderBy;
    }

    public int getOrderBy() {
        return mOrderBy;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setGroupAvator(String groupAvator) {
        mGroupAvator = groupAvator;
    }

    public String getGroupAvator() {
        return mGroupAvator;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }

    public void setColorStyle(int colorStyle) {
        mColorStyle = colorStyle;
    }

    public int getColorStyle() {
        return mColorStyle;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setGroupArticle(int groupArticle) {
        mGroupArticle = groupArticle;
    }

    public int getGroupArticle() {
        return mGroupArticle;
    }

    public void setGroupBoardId(int groupBoardId) {
        mGroupBoardId = groupBoardId;
    }

    public int getGroupBoardId() {
        return mGroupBoardId;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public String getGroupName() {
        return mGroupName;
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

    public void setGroupNote(String groupNote) {
        mGroupNote = groupNote;
    }

    public String getGroupNote() {
        return mGroupNote;
    }

    public void setGroupMember(int groupMember) {
        mGroupMember = groupMember;
    }

    public int getGroupMember() {
        return mGroupMember;
    }

    public void setCreateAt(int createAt) {
        mCreateAt = createAt;
    }

    public int getCreateAt() {
        return mCreateAt;
    }

    public void setGroupCover(String groupCover) {
        mGroupCover = groupCover;
    }

    public String getGroupCover() {
        return mGroupCover;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Long) mId).hashCode();
    }


}