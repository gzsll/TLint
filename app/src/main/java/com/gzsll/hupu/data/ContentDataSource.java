package com.gzsll.hupu.data;

import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;

import java.util.List;

import rx.Observable;

/**
 * Created by sll on 2016/6/3.
 * 帖子详情页数据源
 */
public interface ContentDataSource {

    Observable<ThreadInfo> getThreadInfo(String fid, String tid);

    Observable<List<ThreadReply>> getReplies(String fid, String tid, int page);

    Observable<List<ThreadReply>> getLightReplies(String fid, String tid);
}
