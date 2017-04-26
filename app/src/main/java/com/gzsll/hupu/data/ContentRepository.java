package com.gzsll.hupu.data;

import android.text.TextUtils;

import com.gzsll.hupu.bean.ThreadReplyQuote;
import com.gzsll.hupu.data.local.ContentLocalDataSource;
import com.gzsll.hupu.data.remote.ContentRemoteDataSource;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.util.HtmlUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/6/3.
 */
public class ContentRepository implements ContentDataSource {

    private final ContentRemoteDataSource mContentRemoteDataSource;
    private final ContentLocalDataSource mContentLocalDataSource;

    @Inject
    public ContentRepository(ContentRemoteDataSource mContentRemoteDataSource,
                             ContentLocalDataSource mContentLocalDataSource) {
        this.mContentRemoteDataSource = mContentRemoteDataSource;
        this.mContentLocalDataSource = mContentLocalDataSource;
    }

    @Override
    public Observable<ThreadInfo> getThreadInfo(String fid, String tid) {
        Observable<ThreadInfo> remote = mContentRemoteDataSource.getThreadInfo(fid, tid);
        Observable<ThreadInfo> local = mContentLocalDataSource.getThreadInfo(fid, tid);

        Observable<ThreadInfo> remoteWithLocalUpdate = remote.doOnNext(new Action1<ThreadInfo>() {
            @Override
            public void call(ThreadInfo threadInfo) {
                if (threadInfo != null && threadInfo.getError() == null) {
                    threadInfo.setForumName(threadInfo.getForum().getName());
                    mContentLocalDataSource.saveThreadInfo(threadInfo);
                }
            }
        });
        return Observable.concat(remoteWithLocalUpdate, local).first(new Func1<ThreadInfo, Boolean>() {
            @Override
            public Boolean call(ThreadInfo threadInfo) {
                return threadInfo != null;
            }
        });
    }

    @Override
    public Observable<List<ThreadReply>> getReplies(String fid, final String tid, final int page) {
        Observable<List<ThreadReply>> remote = mContentRemoteDataSource.getReplies(fid, tid, page);
        Observable<List<ThreadReply>> local = mContentLocalDataSource.getReplies(fid, tid, page);

        Observable<List<ThreadReply>> remoteWithLocalUpdate =
                remote.doOnNext(new Action1<List<ThreadReply>>() {
                    @Override
                    public void call(List<ThreadReply> threadReplies) {
                        if (threadReplies != null) {
                            for (ThreadReply reply : threadReplies) {
                                reply.setTid(tid);
                                reply.setIsLight(false);
                                reply.setPageIndex(page);
                                saveReply(reply);
                            }
                        }
                    }
                });

        return Observable.concat(remoteWithLocalUpdate, local)
                .first(new Func1<List<ThreadReply>, Boolean>() {
                    @Override
                    public Boolean call(List<ThreadReply> threadReplies) {
                        return threadReplies != null;
                    }
                });
    }

    @Override
    public Observable<List<ThreadReply>> getLightReplies(String fid, final String tid) {
        Observable<List<ThreadReply>> remote = mContentRemoteDataSource.getLightReplies(fid, tid);
        Observable<List<ThreadReply>> local = mContentLocalDataSource.getLightReplies(fid, tid);

        Observable<List<ThreadReply>> remoteWithLocalUpdate =
                remote.doOnNext(new Action1<List<ThreadReply>>() {
                    @Override
                    public void call(List<ThreadReply> threadReplies) {
                        if (threadReplies != null) {
                            for (ThreadReply reply : threadReplies) {
                                reply.setTid(tid);
                                reply.setIsLight(true);
                                saveReply(reply);
                            }
                        }
                    }
                });
        return Observable.concat(remoteWithLocalUpdate, local)
                .first(new Func1<List<ThreadReply>, Boolean>() {
                    @Override
                    public Boolean call(List<ThreadReply> threadReplies) {
                        return threadReplies != null;
                    }
                });
    }

    private void saveReply(ThreadReply reply) {
        if (!reply.getQuote().isEmpty()) {
            ThreadReplyQuote quote = reply.getQuote().get(0);
            reply.setQuoteHeader(quote.header.get(0));
            if (!TextUtils.isEmpty(quote.togglecontent)) {
                reply.setQuoteToggle(quote.togglecontent);
            }
            reply.setQuoteContent(HtmlUtil.transImgToLocal(quote.content));
        }
        mContentLocalDataSource.saveThreadReply(reply);
    }
}
