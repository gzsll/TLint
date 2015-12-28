package com.gzsll.hupu.support.storage.bean;

import com.gzsll.hupu.support.db.Board;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardList implements Serializable {
    public ArrayList<Board> data;
    public int weight;
    public String name;
}