package com.gzsll.hupu.bean;

import com.gzsll.hupu.db.Forum;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sll on 2016/3/8.
 */
public class Forums implements Serializable {
    public ArrayList<Forum> data;
    public int weight;
    public String name;
}
