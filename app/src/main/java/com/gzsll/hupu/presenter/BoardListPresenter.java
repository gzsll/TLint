package com.gzsll.hupu.presenter;

import android.content.Context;
import android.content.Intent;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.service.OffLineService;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.db.BoardDao;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.BoardList;
import com.gzsll.hupu.support.storage.bean.BoardListData;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
import com.gzsll.hupu.support.storage.bean.Boards;
import com.gzsll.hupu.support.storage.bean.CategoryList;
import com.gzsll.hupu.support.storage.bean.MyBoardListData;
import com.gzsll.hupu.support.storage.bean.MyBoardListResult;
import com.gzsll.hupu.support.utils.NetWorkHelper;
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


    @Inject
    ThreadApi mThreadApi;
    @Inject
    BoardDao mBoardDao;
    @Inject
    Bus mBus;
    @Inject
    Context mContext;
    @Inject
    NetWorkHelper mNetWorkHelper;

    Logger logger = Logger.getLogger(BoardListPresenter.class.getSimpleName());

    private ArrayList<Board> boards;
    private int boardId;

    /**
     * 通过板块id或者子版块
     *
     * @param boardId 板块id
     */
    public void onBoardListReceive(final int boardId) {
        this.boardId = boardId;
        view.showLoading();
        loadFromNet();
    }

    private void loadFromDb() {
        List<Board> boards = mBoardDao.queryBuilder().where(BoardDao.Properties.BoardId.eq(boardId)).orderAsc(BoardDao.Properties.CategoryId).orderAsc(BoardDao.Properties.BoardIndex).list();
        if (!boards.isEmpty()) {
            this.boards = (ArrayList) boards;
            sortBoard(boards);
        } else {
            loadFromNet();
        }
    }

    private void loadFromNet() {
        if (boardId == 0) {
            mThreadApi.getMyBoardList(new Callback<MyBoardListResult>() {
                @Override
                public void success(MyBoardListResult boardListResult, Response response) {
                    if (boardListResult.data != null) {
                        ArrayList<BoardList> boardLists = new ArrayList<BoardList>();
                        boardLists.add(convertMyBoardListData(boardListResult.data));
                        view.renderBoardList(boardLists);
                        view.hideLoading();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    view.onError();
                }
            });
        } else {
            mThreadApi.getBoardList(new Callback<BoardListResult>() {
                @Override
                public void success(BoardListResult boardListResult, Response response) {
                    for (BoardListData data : boardListResult.data) {
                        if (data.fid.equals(String.valueOf(boardId))) {
                            view.renderBoardList(data.sub);
                            view.hideLoading();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    view.onError();
                }
            });
        }
    }

    private BoardList convertMyBoardListData(MyBoardListData data) {
        BoardList boardList = new BoardList();
        boardList.data = data.sub;
        boardList.name = data.name;
        return boardList;
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
        // view.renderBoardList(boardsList);
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

    private Board saveToBoard(CategoryList categoryList, int index, long boardId) {
        Board board = new Board();
        board.setBoardId(boardId);
        board.setBoardIcon(categoryList.getGroupAvator());
        board.setBoardName(categoryList.getGroupName());
        board.setCategoryId(categoryList.getCategoryId());
        board.setCategoryName(categoryList.getCategoryName());
        board.setGroupId(categoryList.getId());
        board.setBoardIndex(index);
        List<Board> boards = mBoardDao.queryBuilder().where(BoardDao.Properties.BoardId.eq(boardId), BoardDao.Properties.GroupId.eq(categoryList.getId())).list();
        if (!boards.isEmpty()) {
            board.setId(boards.get(0).getId());
        }
        mBoardDao.insertOrReplace(board);
        return board;
    }


    public void delGroupAttention(String fid) {
        mThreadApi.delGroupAttention(fid, new Callback<BaseResult>() {
            @Override
            public void success(BaseResult baseResult, Response response) {
                view.showToast(baseResult.getMsg());
                if (baseResult.getStatus() == 200) {

                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * offline group
     *
     * @param board
     */
    public void offlineGroup(Board board) {
        ArrayList<Board> boards = new ArrayList<>();
        boards.add(board);
        offlineGroups(boards);
    }


    /**
     * offline all group under current board
     */
    public void offlineGroups() {
        offlineGroups(boards);
    }

    private void offlineGroups(ArrayList<Board> boards) {
        Intent intent = new Intent(mContext, OffLineService.class);
        intent.putExtra("boards", boards);
        intent.setAction(OffLineService.START_DOWNLOAD);
        mContext.startService(intent);
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

    public void onReload() {
        onBoardListReceive(boardId);
    }
}
