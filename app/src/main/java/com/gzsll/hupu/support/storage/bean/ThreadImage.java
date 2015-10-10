package com.gzsll.hupu.support.storage.bean;

import java.util.List;

/**
 * Created by sll on 2015/6/18.
 */
public class ThreadImage {

    /**
     * imgs : ["http://bbsmobile.hupucdn.com/19512162_byte35KBbyte_11ffc7bb913f77947240ec9ce84524bb_w724h465.png"]
     * index : 0.0
     */
    private List<String> imgs;
    private double index;

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public double getIndex() {
        return index;
    }
}
