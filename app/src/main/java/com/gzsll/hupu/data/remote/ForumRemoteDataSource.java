package com.gzsll.hupu.data.remote;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Forums;
import com.gzsll.hupu.bean.ForumsData;
import com.gzsll.hupu.bean.ForumsResult;
import com.gzsll.hupu.bean.MyForumsData;
import com.gzsll.hupu.bean.MyForumsResult;
import com.gzsll.hupu.data.ForumDataSource;
import com.gzsll.hupu.db.Forum;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sll on 2016/6/3.
 */
public class ForumRemoteDataSource implements ForumDataSource {

    private final ForumApi mForumApi;

    @Inject
    public ForumRemoteDataSource(ForumApi mForumApi) {
        this.mForumApi = mForumApi;
    }

    @Override
    public Observable<List<Forum>> getForumList(final String forumId) {
        if (forumId.equals("0")) {
            return mForumApi.getMyForums().map(new Func1<MyForumsData, List<Forum>>() {
                @Override
                public List<Forum> call(MyForumsData myForumsData) {
                    if (myForumsData != null && myForumsData.data != null) {
                        MyForumsResult data = myForumsData.data;
                        for (Forum forum : data.sub) {
                            forum.setForumId(data.fid);
                            forum.setCategoryName(data.name);
                            forum.setWeight(1);
                        }
                        return data.sub;
                    }
                    return null;
                }
            }).onErrorReturn(new Func1<Throwable, List<Forum>>() {
                @Override
                public List<Forum> call(Throwable throwable) {
                    return null;
                }
            });
        } else {
            return mForumApi.getForums().map(new Func1<ForumsData, List<Forum>>() {
                @Override
                public List<Forum> call(ForumsData forumsData) {
                    if (forumsData != null) {
                        List<Forum> forumList = new ArrayList<>();
                        for (ForumsResult data : forumsData.data) {
                            if (data.fid.equals(forumId)) {
                                for (Forums forums : data.sub) {
                                    for (Forum forum : forums.data) {
                                        forum.setForumId(data.fid);
                                        forum.setCategoryName(forums.name);
                                        forum.setWeight(forums.weight);
                                        forumList.add(forum);
                                    }
                                }
                            }
                        }
                        return forumList;
                    }
                    return null;
                }
            }).onErrorReturn(new Func1<Throwable, List<Forum>>() {
                @Override
                public List<Forum> call(Throwable throwable) {
                    return null;
                }
            });
        }
    }

    @Override
    public Observable<Boolean> removeForum(String fid) {
        return null;
    }
}
