package com.gzsll.hupu.support.storage.bean;

import java.io.Serializable;

/**
 * Created by sll on 2015/11/23.
 */
public class OfflinePicture implements Serializable {
    private String thumb;// 缩略图
    private String url;
    private long length;// 图片大小

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
