package com.gzsll.hupu.otto;

import com.gzsll.hupu.support.storage.bean.Notice;

/**
 * Created by sll on 2015/9/13.
 */
public class ReceiveNoticeEvent {
    private Notice notice;

    public ReceiveNoticeEvent(Notice notice) {
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }
}
