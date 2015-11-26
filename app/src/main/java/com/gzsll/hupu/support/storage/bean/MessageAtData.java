package com.gzsll.hupu.support.storage.bean;

import java.util.List;

/**
 * Created by admin on 2015/9/8 0008.
 */
public class MessageAtData {
    private String lastId;
    private List<MessageAt> list;

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public List<MessageAt> getList() {
        return list;
    }

    public void setList(List<MessageAt> list) {
        this.list = list;
    }
}
