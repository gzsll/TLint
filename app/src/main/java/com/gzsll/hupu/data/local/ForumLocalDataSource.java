package com.gzsll.hupu.data.local;

import com.gzsll.hupu.data.ForumDataSource;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.ForumDao;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/6/3.
 */
public class ForumLocalDataSource implements ForumDataSource {
    private final ForumDao mForumDao;

    @Inject
    public ForumLocalDataSource(ForumDao mForumDao) {
        this.mForumDao = mForumDao;
    }

    @Override
    public Observable<List<Forum>> getForumList(final String forumId) {
        return Observable.create(new Observable.OnSubscribe<List<Forum>>() {
            @Override
            public void call(Subscriber<? super List<Forum>> subscriber) {
                List<Forum> forumList =
                        mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forumId)).list();
                subscriber.onNext(forumList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public void saveForum(Forum forum) {
        mForumDao.queryBuilder()
                .where(ForumDao.Properties.ForumId.eq(forum.getForumId()),
                        ForumDao.Properties.Fid.eq(forum.getFid()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        mForumDao.insert(forum);
    }

    @Override
    public Observable<Boolean> removeForum(String fid) {
        return null;
    }
}
