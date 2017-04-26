package com.gzsll.hupu.data;

import com.gzsll.hupu.Logger;
import com.gzsll.hupu.data.local.ForumLocalDataSource;
import com.gzsll.hupu.data.remote.ForumRemoteDataSource;
import com.gzsll.hupu.db.Forum;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/6/3.
 */
public class ForumRepository implements ForumDataSource {


    private final ForumLocalDataSource mForumLocalDataSource;
    private final ForumRemoteDataSource mForumRemoteDataSource;

    @Inject
    public ForumRepository(ForumLocalDataSource mForumLocalDataSource,
                           ForumRemoteDataSource mForumRemoteDataSource) {
        this.mForumLocalDataSource = mForumLocalDataSource;
        this.mForumRemoteDataSource = mForumRemoteDataSource;
    }

    @Override
    public Observable<List<Forum>> getForumList(String forumId) {
        Logger.d("getForumList:" + forumId);
        Observable<List<Forum>> remote = mForumRemoteDataSource.getForumList(forumId);
        Observable<List<Forum>> local = mForumLocalDataSource.getForumList(forumId);

        Observable<List<Forum>> remoteWithLocalUpdate = remote.doOnNext(new Action1<List<Forum>>() {
            @Override
            public void call(List<Forum> fora) {
                if (fora != null) {
                    for (Forum forum : fora) {
                        mForumLocalDataSource.saveForum(forum);
                    }
                }
            }
        });
        if (forumId.equals("0")) {  //我的论坛强制刷新
            return remoteWithLocalUpdate;
        }

        return Observable.concat(local, remoteWithLocalUpdate).first(new Func1<List<Forum>, Boolean>() {
            @Override
            public Boolean call(List<Forum> fora) {
                return fora != null && !fora.isEmpty();
            }
        });
    }

    @Override
    public Observable<Boolean> removeForum(String fid) {
        return null;
    }
}
