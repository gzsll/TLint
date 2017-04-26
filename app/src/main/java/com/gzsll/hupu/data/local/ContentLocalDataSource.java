package com.gzsll.hupu.data.local;

import com.gzsll.hupu.data.ContentDataSource;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadInfoDao;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.db.ThreadReplyDao;
import com.gzsll.hupu.util.HtmlUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/6/3.
 */
public class ContentLocalDataSource implements ContentDataSource {

    private final ThreadInfoDao mThreadInfoDao;
    private final ThreadReplyDao mThreadReplyDao;

    @Inject
    public ContentLocalDataSource(ThreadInfoDao mThreadInfoDao, ThreadReplyDao mThreadReplyDao) {
        this.mThreadInfoDao = mThreadInfoDao;
        this.mThreadReplyDao = mThreadReplyDao;
    }

    @Override
    public Observable<ThreadInfo> getThreadInfo(String fid, final String tid) {
        return Observable.create(new Observable.OnSubscribe<ThreadInfo>() {
            @Override
            public void call(Subscriber<? super ThreadInfo> subscriber) {
                List<ThreadInfo> threadInfos =
                        mThreadInfoDao.queryBuilder().where(ThreadInfoDao.Properties.Tid.eq(tid)).list();
                if (!threadInfos.isEmpty()) {
                    ThreadInfo threadInfo = threadInfos.get(0);
                    String content = threadInfo.getContent();
                    threadInfo.setContent(HtmlUtil.transImgToLocal(content));
                    subscriber.onNext(threadInfo);
                } else {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public void saveThreadInfo(ThreadInfo mThreadInfo) {
        mThreadInfoDao.queryBuilder()
                .where(ThreadInfoDao.Properties.Tid.eq(mThreadInfo.getTid()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        mThreadInfoDao.insert(mThreadInfo);
    }

    @Override
    public Observable<List<ThreadReply>> getReplies(String fid, final String tid, final int page) {
        return Observable.create(new Observable.OnSubscribe<List<ThreadReply>>() {
            @Override
            public void call(Subscriber<? super List<ThreadReply>> subscriber) {
                List<ThreadReply> replies = mThreadReplyDao.queryBuilder()
                        .where(ThreadReplyDao.Properties.Tid.eq(tid),
                                ThreadReplyDao.Properties.IsLight.eq(false),
                                ThreadReplyDao.Properties.PageIndex.eq(page))
                        .orderAsc(ThreadReplyDao.Properties.Floor)
                        .list();
                if (replies.isEmpty() && page > 1) {
                    subscriber.onNext(null);
                } else {
                    subscriber.onNext(replies);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<ThreadReply>> getLightReplies(String fid, final String tid) {
        return Observable.create(new Observable.OnSubscribe<List<ThreadReply>>() {
            @Override
            public void call(Subscriber<? super List<ThreadReply>> subscriber) {
                List<ThreadReply> replies = mThreadReplyDao.queryBuilder()
                        .where(ThreadReplyDao.Properties.Tid.eq(tid),
                                ThreadReplyDao.Properties.IsLight.eq(true))
                        .orderDesc(ThreadReplyDao.Properties.Light_count)
                        .list();
                subscriber.onNext(replies);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public void saveThreadReply(ThreadReply reply) {
        mThreadReplyDao.queryBuilder()
                .where(ThreadReplyDao.Properties.Pid.eq(reply.getPid()),
                        ThreadReplyDao.Properties.IsLight.eq(reply.getIsLight()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        mThreadReplyDao.insert(reply);
    }
}
