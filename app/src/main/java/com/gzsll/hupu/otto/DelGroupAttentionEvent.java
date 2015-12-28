package com.gzsll.hupu.otto;

/**
 * Created by sll on 2015/11/25.
 */
public class DelGroupAttentionEvent {
    private String fid;

    public DelGroupAttentionEvent(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }
}
