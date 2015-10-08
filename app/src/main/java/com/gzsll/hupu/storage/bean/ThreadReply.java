package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ThreadReply {

    private static final String FIELD_PAGECOUNT = "pagecount";
    private static final String FIELD_PAGE = "page";
    private static final String FIELD_LIST = "list";


    @SerializedName(FIELD_PAGECOUNT)
    private int mPagecount;
    @SerializedName(FIELD_PAGE)
    private int mPage;
    @SerializedName(FIELD_LIST)
    private List<ThreadReplyItem> mLists;


    public ThreadReply() {

    }

    public void setPagecount(int pagecount) {
        mPagecount = pagecount;
    }

    public int getPagecount() {
        return mPagecount;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPage() {
        return mPage;
    }

    public void setLists(List<ThreadReplyItem> lists) {
        mLists = lists;
    }

    public List<ThreadReplyItem> getLists() {
        return mLists;
    }


}