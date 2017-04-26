package com.gzsll.hupu.bean;

/**
 * 图片实体
 * Created by sll on 2015/5/19.
 */
public class Image {
    public String path;
    public String name;
    public long time;
    public int type;
    public boolean checked = false;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
