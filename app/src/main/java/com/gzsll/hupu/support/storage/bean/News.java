package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/10/10.
 */
public class News {

    /**
     * nid : 1960311
     * title : 韦特斯透露自己罚球时偏右站原因
     * summary : 韦特斯罚球时会往右边多站一步，他称这是由于他的投篮会偏左。
     * uptime : 1444467594
     * img : http://c1.hoopchina.com.cn/uploads/star/event/images/151010/thumbnail-2142e88097b80941357ca56bb3a16e3150192f4c.png
     * type : 1
     * lights : 0
     * replies : 132
     * read : 1960311
     */

    private String nid;
    private String title;
    private String summary;
    private String uptime;
    private String img;
    private int type;
    private int lights;
    private String replies;
    private String read;

    public void setNid(String nid) {
        this.nid = nid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLights(int lights) {
        this.lights = lights;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUptime() {
        return uptime;
    }

    public String getImg() {
        return img;
    }

    public int getType() {
        return type;
    }

    public int getLights() {
        return lights;
    }

    public String getReplies() {
        return replies;
    }

    public String getRead() {
        return read;
    }
}
