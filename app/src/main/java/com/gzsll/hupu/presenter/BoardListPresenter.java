package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.otto.ReceiveNoticeEvent;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.db.BoardDao;
import com.gzsll.hupu.support.storage.bean.BoardList;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.Boards;
import com.gzsll.hupu.support.storage.bean.CategoryList;
import com.gzsll.hupu.support.storage.bean.GroupList;
import com.gzsll.hupu.view.BoardListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/5/27.
 */
@Singleton
public class BoardListPresenter extends Presenter<BoardListView> {

    private static final int USER_BOARDID = 0;


    @Inject
    ThreadApi mThreadApi;
    @Inject
    BoardDao mBoardDao;
    @Inject
    Bus mBus;

    Logger logger = Logger.getLogger(BoardListPresenter.class.getSimpleName());


    /**
     * 通过板块id或者子版块
     *
     * @param groupId
     */
    public void onBoardListReceive(final int groupId) {
        view.showLoading();
        List<Board> boards = mBoardDao.queryBuilder().where(BoardDao.Properties.GroupId.eq(groupId)).orderAsc(BoardDao.Properties.CategoryId).orderAsc(BoardDao.Properties.BoardIndex).list();
        if (!boards.isEmpty() && groupId != 0) {
            sortBoard(boards);
        } else {
            mThreadApi.getBoardList(new Callback<BoardListResult>() {
                @Override
                public void success(BoardListResult boardListResult, Response response) {
                    if (boardListResult.getStatus() == 200) {
                        for (int i = 0; i < boardListResult.getData().getBoardLists().size(); i++) {
                            BoardList boardList = boardListResult.getData().getBoardLists().get(i);
                            if (boardList.getId() == groupId) {
                                List<Boards> boardsList = new ArrayList<Boards>();
                                for (int j = 0; j < boardList.getGroupLists().size(); j++) {
                                    GroupList groupList = boardList.getGroupLists().get(j);
                                    Boards boards = new Boards();
                                    boards.setName(groupList.getCategoryName());
                                    List<Board> boardArrayList = new ArrayList<Board>();
                                    for (int k = 0; k < groupList.getCategoryLists().size(); k++) {
                                        CategoryList categoryList = groupList.getCategoryLists().get(k);
                                        Board board = saveToBoard(categoryList, k, groupId);
                                        boardArrayList.add(board);
                                    }
                                    boards.setBoards(boardArrayList);
                                    boardsList.add(boards);
                                }
                                view.renderBoardList(boardsList);
                                view.hideLoading();
                            }
                        }
                        if (boardListResult.getNotice() != null) {
                            mBus.post(new ReceiveNoticeEvent(boardListResult.getNotice()));
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }


    }


    private void sortBoard(List<Board> items) {
        List<Boards> boardsList = new ArrayList<>();
        LinkedHashMap<String, List<Board>> map = group(items, new GroupBy<String>() {
            @Override
            public String groupby(Object obj) {
                Board item = (Board) obj;
                return item.getCategoryName();
            }
        });
        for (String key : map.keySet()) {
            List<Board> list = map.get(key);
            Boards boards = new Boards();
            boards.setBoards(list);
            boards.setName(key);
            boardsList.add(boards);
        }
        view.renderBoardList(boardsList);
        view.hideLoading();

    }


    public interface GroupBy<T> {
        T groupby(Object obj);
    }


    public <T extends Comparable<T>, D> LinkedHashMap<T, List<D>> group(Collection<D> colls, GroupBy<T> gb) {
        if (colls == null || colls.isEmpty()) {
            return null;
        }
        if (gb == null) {
            return null;
        }
        Iterator<D> iterator = colls.iterator();
        LinkedHashMap<T, List<D>> map = new LinkedHashMap<T, List<D>>();
        while (iterator.hasNext()) {
            D d = iterator.next();
            T t = gb.groupby(d);
            if (map.containsKey(t)) {
                map.get(t).add(d);
            } else {
                List<D> list = new ArrayList<D>();
                list.add(d);
                map.put(t, list);
            }
        }
        return map;
    }

    private Board saveToBoard(CategoryList categoryList, int index, int groupId) {
        Board board = new Board();
        board.setBoardId(categoryList.getId());
        board.setBoardIcon(categoryList.getGroupAvator());
        board.setBoardName(categoryList.getGroupName());
        board.setCategoryId(Long.valueOf(categoryList.getCategoryId()));
        board.setCategoryName(categoryList.getCategoryName());
        board.setGroupId(Long.valueOf(groupId));
        board.setBoardIndex(index);
        mBoardDao.insertOrReplace(board);
        return board;
    }


    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
