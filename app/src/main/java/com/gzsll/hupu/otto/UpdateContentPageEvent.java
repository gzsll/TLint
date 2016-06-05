package com.gzsll.hupu.otto;

/**
 * Created by sll on 2016/6/3.
 */
public class UpdateContentPageEvent {

    private int page;
    private int totalPage;

    public UpdateContentPageEvent(int page, int totalPage) {
        this.page = page;
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
