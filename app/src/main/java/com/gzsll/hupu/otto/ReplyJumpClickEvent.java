package com.gzsll.hupu.otto;

/**
 * Created by sll on 2015/6/18.
 */
public class ReplyJumpClickEvent {
    private int currentPage;

    public ReplyJumpClickEvent(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
