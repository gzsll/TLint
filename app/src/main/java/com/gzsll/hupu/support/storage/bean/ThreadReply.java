package com.gzsll.hupu.support.storage.bean;

import java.util.List;


public class ThreadReply {


    private int pagecount;
    private int page;
    private List<ThreadReplyItem> list;


    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ThreadReplyItem> getList() {
        return list;
    }

    public void setList(List<ThreadReplyItem> list) {
        this.list = list;
    }
}