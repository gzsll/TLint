package com.gzsll.hupu.support.storage.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardListData implements Serializable {
    public String fid;
    public String name;
    public ArrayList<BoardList> sub;
}