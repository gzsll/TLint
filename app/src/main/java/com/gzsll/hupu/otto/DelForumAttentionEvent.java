package com.gzsll.hupu.otto;

/**
 * Created by sll on 2015/11/25.
 */
public class DelForumAttentionEvent {
    private String fid;

    public DelForumAttentionEvent(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }
}
