package com.gzsll.hupu.data.remote;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.ThreadLightReplyData;
import com.gzsll.hupu.bean.ThreadReplyData;
import com.gzsll.hupu.data.ContentDataSource;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/6/3.
 */
public class ContentRemoteDataSource implements ContentDataSource {

    private final ForumApi mForumApi;

    @Inject
    public ContentRemoteDataSource(ForumApi mForumApi) {
        this.mForumApi = mForumApi;
    }

    @Override
    public Observable<ThreadInfo> getThreadInfo(String fid, String tid) {
        return mForumApi.getThreadInfo(tid, fid, 1, "")
                .onErrorReturn(new Func1<Throwable, ThreadInfo>() {
                    @Override
                    public ThreadInfo call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<ThreadReply>> getReplies(String fid, String tid, int page) {
        return mForumApi.getThreadReplyList(tid, fid, page)
                .map(new Func1<ThreadReplyData, List<ThreadReply>>() {
                    @Override
                    public List<ThreadReply> call(ThreadReplyData threadReplyData) {
                        if (threadReplyData != null && threadReplyData.status == 200) {
                            return threadReplyData.result.list;
                        }
                        return null;
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<ThreadReply>>() {
                    @Override
                    public List<ThreadReply> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<ThreadReply>> getLightReplies(String fid, String tid) {
        return mForumApi.getThreadLightReplyList(tid, fid)
                .map(new Func1<ThreadLightReplyData, List<ThreadReply>>() {
                    @Override
                    public List<ThreadReply> call(ThreadLightReplyData threadLightReplyData) {
                        if (threadLightReplyData != null && threadLightReplyData.status == 200) {
                            return threadLightReplyData.list;
                        }
                        return null;
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<ThreadReply>>() {
                    @Override
                    public List<ThreadReply> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
