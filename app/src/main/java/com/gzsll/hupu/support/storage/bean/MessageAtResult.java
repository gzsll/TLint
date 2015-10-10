package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageAtResult {
    private int status;
    private String msg;
    private MessageAtData data;
    private int uid;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(MessageAtData data) {
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

    public MessageAtData getData() {
        return data;
    }

    public int getUid() {
        return uid;
    }


/**
 *
 {"status":200,"msg":"ok","data":{"list":[{"id":168461733,"uid":4847679,"type":3,"groupThreadId":3143842,"groupReplyId":141142068,"userInfo":{"uid":18463896,"username":"sllbeta","icon":"http:\/\/b1.hoopchina.com.cn\/images\/default_hupu_app3.0.png","sex":0,"banned":0,"level":2,"synctime":1441696759,"groups":"8","postNum":0,"favoriteNum":0,"badge":{"small":["http:\/\/bbsmobile.hupucdn.com\/1432198236.8985-post-gray.png","http:\/\/bbsmobile.hupucdn.com\/1432198524.5019-reply-gray.png","http:\/\/bbsmobile.hupucdn.com\/1432198901.1229-light-gray.png"]},"replyNum":0},"formatTime":"7\u5206\u949f\u524d","reply":{"id":141142068,"pid":171068,"content":"<blockquote>\u5f15\u75284\u697c @<a href=\"http:\/\/my.hupu.com\/4847679\" color=\"005eac\">pursll<\/a> \u53d1\u8868\u7684:test .....<\/blockquote>test","lights":0,"create_at":"7\u5206\u949f\u524d","groupThreadId":3143842,"userInfo":{"uid":18463896,"username":"sllbeta","icon":"http:\/\/b1.hoopchina.com.cn\/images\/default_hupu_app3.0.png","sex":0,"banned":0,"level":2,"synctime":1441696759,"groups":"8","postNum":0,"favoriteNum":0,"badge":{"small":["http:\/\/bbsmobile.hupucdn.com\/1432198236.8985-post-gray.png","http:\/\/bbsmobile.hupucdn.com\/1432198524.5019-reply-gray.png","http:\/\/bbsmobile.hupucdn.com\/1432198901.1229-light-gray.png"]},"replyNum":0},"formatTime":"7\u5206\u949f\u524d","addtime":1441704967,"isLight":0,"floor":6}}],"lastId":""},"uid":4847679}
 */


}
