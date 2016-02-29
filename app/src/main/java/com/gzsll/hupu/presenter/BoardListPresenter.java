package com.gzsll.hupu.presenter;

import android.content.Context;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.db.Board;
import com.gzsll.hupu.support.db.BoardDao;
import com.gzsll.hupu.support.storage.bean.AttendStatusResult;
import com.gzsll.hupu.support.storage.bean.BoardList;
import com.gzsll.hupu.support.storage.bean.BoardListData;
import com.gzsll.hupu.support.storage.bean.BoardListResult;
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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    public void onBoardListReceive(int boardId) {
        this.boardId = boardId;
        view.showLoading();
        List<Board> boards = mBoardDao.queryBuilder().where(BoardDao.Properties.BoardId.eq(boardId)).list();
        if (!boards.isEmpty()) {
            this.boards = (ArrayList) boards;
            sortBoard(boards);
        } else {
            loadFromNet();
        }
    }


    private void loadFromNet() {
        if (boardId == 0) {
            mThreadApi.getMyBoardList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<MyBoardListResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    view.onError();
                }

                @Override
                public void onNext(MyBoardListResult result) {
                    if (result.data != null) {
                        ArrayList<BoardList> boardLists = new ArrayList<BoardList>();
                        boardLists.add(convertMyBoardListData(result.data));
                        view.renderBoardList(boardLists);
                        view.hideLoading();
                    }
                }
            });
        } else {
            mThreadApi.getBoardList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BoardListResult>() {
                @Override
                public void call(BoardListResult boardListResult) {
                    for (BoardListData data : boardListResult.data) {
                        if (data.fid.equals(String.valueOf(boardId))) {
                            view.renderBoardList(data.sub);
                            view.hideLoading();
                            saveToBoard(data.sub, data.fid);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
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
        List<BoardList> boardsList = new ArrayList<>();
        LinkedHashMap<String, List<Board>> map = group(items, new GroupBy<String>() {
            @Override
            public String groupby(Object obj) {
                Board item = (Board) obj;
                return item.getCategoryName();
            }
        });
        for (String key : map.keySet()) {
            List<Board> list = map.get(key);
            BoardList board = new BoardList();
            board.data = (ArrayList) map.get(key);
            board.name = key;
            boardsList.add(board);
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

    private void saveToBoard(ArrayList<BoardList> boardLists, String boardId) {
        for (BoardList boardList : boardLists) {
            for (Board board : boardList.data) {
                List<Board> boards = mBoardDao.queryBuilder().where(BoardDao.Properties.BoardId.eq(boardId), BoardDao.Properties.Fid.eq(board.getFid())).list();
                if (!boards.isEmpty()) {
                    board.setId(boards.get(0).getId());
                }
                board.setBoardId(boardId);
                board.setCategoryName(boardList.name);
                mBoardDao.insertOrReplace(board);
            }
        }
    }


    public void delGroupAttention(String fid) {
        mThreadApi.delGroupAttention(fid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AttendStatusResult>() {
            @Override
            public void call(AttendStatusResult result) {
                if (result.status == 200 && result.result == 1) {
                    view.showToast("取消关注成功");
                    onReload();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showToast("取消关注失败，请检查网络后重试");
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
//        Intent intent = new Intent(mContext, OffLineService.class);
//        intent.putExtra("boards", boards);
//        intent.setAction(OffLineService.START_DOWNLOAD);
//        mContext.startService(intent);
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
