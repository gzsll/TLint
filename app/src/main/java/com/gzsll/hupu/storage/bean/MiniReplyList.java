package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class MiniReplyList implements Serializable {

    private static final String FIELD_COUNT = "count";
    private static final String FIELD_PAGECOUNT = "pagecount";
    private static final String FIELD_PAGE = "page";
    private static final String FIELD_LIST = "list";


    @SerializedName(FIELD_COUNT)
    private int mCount;
    @SerializedName(FIELD_PAGECOUNT)
    private int mPagecount;
    @SerializedName(FIELD_PAGE)
    private int mPage;
    @SerializedName(FIELD_LIST)
    private List<MiniReplyListItem> mLists;


    public MiniReplyList() {

    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getCount() {
        return mCount;
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

    public void setLists(List<MiniReplyListItem> lists) {
        mLists = lists;
    }

    public List<MiniReplyListItem> getLists() {
        return mLists;
    }


}