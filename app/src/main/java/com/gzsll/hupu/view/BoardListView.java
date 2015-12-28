package com.gzsll.hupu.view;

import com.gzsll.hupu.support.storage.bean.BoardList;

import java.util.List;

/**
 * Created by sll on 2015/5/27.
 */
public interface BoardListView extends BaseView {

    void onError();

    void renderBoardList(List<BoardList> boardGroups);

    void showLoginView();
}
