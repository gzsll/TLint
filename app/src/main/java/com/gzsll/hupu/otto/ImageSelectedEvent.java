package com.gzsll.hupu.otto;

import java.util.ArrayList;

/**
 * Created by sll on 2015/3/19.
 */
public class ImageSelectedEvent {
    private ArrayList<String> urls;

    public ImageSelectedEvent(ArrayList<String> urls) {
        this.urls = urls;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }
}
