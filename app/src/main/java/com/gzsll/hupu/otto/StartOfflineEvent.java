package com.gzsll.hupu.otto;

import com.gzsll.hupu.support.db.Board;

/**
 * Created by sll on 2015/11/23.
 */
public class StartOfflineEvent {
    private Board board;

    public StartOfflineEvent() {
    }


    public StartOfflineEvent(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
