package com.gzsll.hupu.data;

import android.text.TextUtils;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.ThreadListData;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.db.ThreadDao;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by sll on 2016/6/3.
 */
public class ThreadRepository {


    private ThreadDao mThreadDao;
    private ForumApi mForumApi;

    @Inject
    public ThreadRepository(ThreadDao mThreadDao, ForumApi mForumApi) {
        this.mThreadDao = mThreadDao;
        this.mForumApi = mForumApi;
    }

    public Observable<List<Thread>> getThreadListObservable(final int type,
                                                            PublishSubject<List<Thread>> mSubject) {
        Observable<List<Thread>> firstObservable = Observable.fromCallable(new Func0<List<Thread>>() {
            @Override
            public List<Thread> call() {
                return mThreadDao.queryBuilder().where(ThreadDao.Properties.Type.eq(type)).list();
            }
        });
        return firstObservable.concatWith(mSubject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThreadListData> getRecommendThreadList(final String lastTid, String lastTamp,
                                                             final PublishSubject<List<Thread>> mSubject) {
        return mForumApi.getRecommendThreadList(lastTid, lastTamp)
                .doOnNext(new Action1<ThreadListData>() {
                    @Override
                    public void call(ThreadListData threadListData) {
                        if (threadListData != null && threadListData.result != null) {
                            cacheThreadList(0, TextUtils.isEmpty(lastTid), threadListData.result.data);
                        }
                        if (mSubject != null) {
                            mSubject.onNext(mThreadDao.queryBuilder()
                                    .where(ThreadDao.Properties.Type.eq(Constants.TYPE_RECOMMEND))
                                    .list());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThreadListData> getThreadsList(final String fid, final String lastTid,
                                                     String lastTamp, String type, final PublishSubject<List<Thread>> mSubject) {
        return mForumApi.getThreadsList(fid, lastTid, lastTamp, type)
                .doOnNext(new Action1<ThreadListData>() {
                    @Override
                    public void call(ThreadListData threadListData) {
                        if (threadListData != null && threadListData.result != null) {
                            cacheThreadList(Integer.valueOf(fid), TextUtils.isEmpty(lastTid),
                                    threadListData.result.data);
                        }
                        if (mSubject != null) {
                            mSubject.onNext(mThreadDao.queryBuilder()
                                    .where(ThreadDao.Properties.Type.eq(Integer.valueOf(fid)))
                                    .list());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void cacheThreadList(int type, boolean clear, List<Thread> threads) {
        if (clear) {
            mThreadDao.queryBuilder()
                    .where(ThreadDao.Properties.Type.eq(type))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
        }
        for (Thread thread : threads) {
            if (mThreadDao.queryBuilder()
                    .where(ThreadDao.Properties.Tid.eq(thread.getTid()), ThreadDao.Properties.Type.eq(type))
                    .count() == 0) {
                thread.setType(type);
                mThreadDao.insert(thread);
            }
        }
    }
}
