package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BoardListData {

    private static final String FIELD_BOARD_LIST = "boardList";


    @SerializedName(FIELD_BOARD_LIST)
    private List<BoardList> mBoardLists;


    public void setBoardLists(List<BoardList> boardLists) {
        mBoardLists = boardLists;
    }

    public List<BoardList> getBoardLists() {
        return mBoardLists;
    }


}