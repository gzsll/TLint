package com.gzsll.hupu.support.storage.bean;

/**
 * Created by gzsll on 2014/5/15 0015.
 */
public class LightResult {
    public LResult result;
    public LError error;


    public class LResult {
        public String light;
    }

    public class LError {
        public String text;
    }
}
