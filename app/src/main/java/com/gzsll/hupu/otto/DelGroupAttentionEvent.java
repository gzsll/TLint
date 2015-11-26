package com.gzsll.hupu.otto;

/**
 * Created by sll on 2015/11/25.
 */
public class DelGroupAttentionEvent {
    private long groupId;

    public DelGroupAttentionEvent(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }
}
