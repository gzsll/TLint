package com.gzsll.hupu.support.storage.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sll on 2015/12/10.
 */
public class MyBoardListData implements Serializable {
    public String fid;
    public String name;
    public ArrayList<Board> sub;
}
