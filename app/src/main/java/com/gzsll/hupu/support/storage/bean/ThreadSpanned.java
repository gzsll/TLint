package com.gzsll.hupu.support.storage.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/8/20.
 */
public class ThreadSpanned {
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_IMG = 2;
    public static final int TYPE_QUOTE = 0;
    public int endIndex;
    public int h;
    public List<ThreadSpanned> quoteSpanneds;
    public String realContent;
    public int startIndex;
    public int type;
    public int w;

    public ThreadSpanned(int type, String realContent, int startIndex, int endIndex) {
        this.type = type;
        if (type == 0) {
            this.quoteSpanneds = new ArrayList<>();
        }
        this.realContent = realContent;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }


}
