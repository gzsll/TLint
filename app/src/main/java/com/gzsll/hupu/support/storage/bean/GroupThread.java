package com.gzsll.hupu.support.storage.bean;

import java.util.ArrayList;
import java.util.List;


public class GroupThread {


    private int uid;
    private int lastReplyTime;
    private int special;
    private double score;
    private String color;
    private int lights;
    private String sharedImg;
    private UserInfo userInfo;
    private int attention;
    private int zan;
    private String username;
    private int type;
    private long id;
    private int digest;
    private long createAtUnixtime;
    private int replies;
    private String createAt;
    private String title;
    private int groupId;
    private String note;
    private List<Cover> cover = new ArrayList<>();
    private int tid;


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Cover> getCover() {
        return cover;
    }

    public void setCover(List<Cover> cover) {
        this.cover = cover;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}