package com.gzsll.hupu.bean;

import com.gzsll.hupu.db.Thread;

import java.util.ArrayList;

/**
 * Created by sll on 2015/12/10.
 */
public class ThreadListResult {
    public String stamp;
    public ArrayList<Thread> data;
    public boolean nextPage;
    public int nextDataExists;
}
