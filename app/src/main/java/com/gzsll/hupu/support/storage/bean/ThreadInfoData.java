package com.gzsll.hupu.support.storage.bean;

public class ThreadInfoData {


    private int favorite;
    private ThreadReply threadReply;
    private ThreadInfo threadInfo;
    private int page;
    private ThreadHotReply threadHotReply;


    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public ThreadReply getThreadReply() {
        return threadReply;
    }

    public void setThreadReply(ThreadReply threadReply) {
        this.threadReply = threadReply;
    }

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ThreadHotReply getThreadHotReply() {
        return threadHotReply;
    }

    public void setThreadHotReply(ThreadHotReply threadHotReply) {
        this.threadHotReply = threadHotReply;
    }
}