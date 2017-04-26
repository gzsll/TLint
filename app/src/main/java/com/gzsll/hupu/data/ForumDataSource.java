package com.gzsll.hupu.data;

import com.gzsll.hupu.db.Forum;

import java.util.List;

import rx.Observable;

/**
 * Created by sll on 2016/6/3.
 */
public interface ForumDataSource {

    Observable<List<Forum>> getForumList(String forumId);

    Observable<Boolean> removeForum(String fid);
}
