package com.gzsll.hupu.storage.bean;

import java.util.List;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageReplyData {
    private String lastId;
    private List<MessageReply> list;

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public List<MessageReply> getList() {
        return list;
    }

    public void setList(List<MessageReply> list) {
        this.list = list;
    }
}
