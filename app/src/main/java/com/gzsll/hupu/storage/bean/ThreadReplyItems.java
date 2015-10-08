package com.gzsll.hupu.storage.bean;

import java.util.List;

/**
 * Created by sll on 2015/6/18.
 */
public class ThreadReplyItems {
    private int headId;
    private String name;
    private int currentPage;
    private int totalPage;
    private List<ThreadReplyItem> mLists;

    public List<ThreadReplyItem> getmLists() {
        return mLists;
    }

    public void setmLists(List<ThreadReplyItem> mLists) {
        this.mLists = mLists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }
}
