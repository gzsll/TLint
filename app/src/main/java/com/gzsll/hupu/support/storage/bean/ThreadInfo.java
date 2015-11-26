package com.gzsll.hupu.support.storage.bean;

public class ThreadInfo {


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(int lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLights() {
        return lights;
    }

    public void setLights(int lights) {
        this.lights = lights;
    }

    public String getSharedImg() {
        return sharedImg;
    }

    public void setSharedImg(String sharedImg) {
        this.sharedImg = sharedImg;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getDigest() {
        return digest;
    }

    public void setDigest(int digest) {
        this.digest = digest;
    }

    public long getCreateAtUnixtime() {
        return createAtUnixtime;
    }

    public void setCreateAtUnixtime(long createAtUnixtime) {
        this.createAtUnixtime = createAtUnixtime;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    private int uid;
    private int lastReplyTime;
    private int special;
    private Groups groups;
    private double score;
    private String color;
    private int lights;
    private String sharedImg;
    private UserInfo userInfo;
    private int attention;
    private int type;
    private int zan;
    private String username;
    private long id;
    private int digest;
    private long createAtUnixtime;
    private int replies;
    private String createAt;
    private String title;
    private long groupId;
    private String note;
    private String content;
    private long tid;




    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}