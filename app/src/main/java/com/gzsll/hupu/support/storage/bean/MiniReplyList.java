package com.gzsll.hupu.support.storage.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MiniReplyList implements Serializable {


    private int count;
    private int pagecount;
    private int page;
    private List<MiniReplyListItem> list = new ArrayList<>();


    public MiniReplyList() {

    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setLists(List<MiniReplyListItem> lists) {
        this.list = lists;
    }

    public List<MiniReplyListItem> getLists() {
        return list;
    }


}