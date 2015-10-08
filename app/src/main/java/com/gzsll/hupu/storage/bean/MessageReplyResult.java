package com.gzsll.hupu.storage.bean;

/**
 * Created by admin on 2015/9/8 0008.
 */
public class MessageReplyResult {

    /**
     * status : 200
     * msg : ok
     * data : {"list":[{"threadInfo":{"id":2869008,"title":"虎扑客户端TL全新适配MaterialDesign，有没有需要的？","groupId":255,"createAt":"2天前"},"groupInfo":{"id":255,"groupName":"步行街","color":"66CC33"},"num":0,"isread":1}],"lastId":""}
     * uid : 4847679
     */

    private int status;
    private String msg;
    private MessageReplyData data;
    private int uid;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(MessageReplyData data) {
        this.data = data;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public MessageReplyData getData() {
        return data;
    }

    public int getUid() {
        return uid;
    }


}
