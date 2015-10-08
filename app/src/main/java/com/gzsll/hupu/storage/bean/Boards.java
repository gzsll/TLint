package com.gzsll.hupu.storage.bean;

import com.gzsll.hupu.db.Board;

import java.util.List;

/**
 * Created by sll on 2015/9/17.
 */
public class Boards {
    private String name;
    private List<Board> boards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }
}
