package com.gzsll.hupu.presenter;

import android.text.TextUtils;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Forums;
import com.gzsll.hupu.bean.ForumsData;
import com.gzsll.hupu.bean.ForumsResult;
import com.gzsll.hupu.bean.MyForumsData;
import com.gzsll.hupu.bean.MyForumsResult;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.ui.view.ForumListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
public class ForumListPresenter extends Presenter<ForumListView> {

    @Inject
    ForumDao mForumDao;
    @Inject
    ForumApi mForumApi;


    @Singleton
    @Inject
    public ForumListPresenter() {
    }



    public void onForumListReceive(final String forumId) {
        view.showLoading();
        getForumObservable(forumId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Forum>>() {
            @Override
            public void call(List<Forum> fora) {
                if (fora == null || fora.isEmpty()) {
                    view.onError();
                } else {
                    view.hideLoading();
                    view.renderForumList(fora);
                }
            }
        });
    }


    private Observable<List<Forum>> getForumObservable(String forumId) {
        return Observable.just(forumId).flatMap(new Func1<String, Observable<List<Forum>>>() {
            @Override
            public Observable<List<Forum>> call(String s) {
                if (TextUtils.equals(s, "0")) {
                    return loadUserForums();
                } else {
                    return loadAllForums(s);
                }
            }
        });
    }

    private Observable<List<Forum>> loadUserForums() {
        return mForumApi.getMyForums().flatMap(new Func1<MyForumsData, Observable<List<Forum>>>() {
            @Override
            public Observable<List<Forum>> call(MyForumsData result) {
                if (result != null && result.data != null) {
                    MyForumsResult data = result.data;
                    for (Forum forum : data.sub) {
                        forum.setForumId(data.fid);
                        forum.setCategoryName(data.name);
                        forum.setWeight(1);
                    }
                    return Observable.just(data.sub);
                }

                return null;
            }
        }).doOnNext(new Action1<List<Forum>>() {
            @Override
            public void call(List<Forum> fora) {
                saveToDb(fora);
            }
        }).onErrorReturn(new Func1<Throwable, List<Forum>>() {
            @Override
            public List<Forum> call(Throwable throwable) {
                return mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq("0")).list();
            }
        });
    }

    private Observable<List<Forum>> loadAllForums(final String forumId) {
        return mForumApi.getForums().flatMap(new Func1<ForumsData, Observable<List<Forum>>>() {
            @Override
            public Observable<List<Forum>> call(ForumsData result) {
                if (result != null) {
                    for (ForumsResult data : result.data) {
                        if (data.fid.equals(forumId)) {
                            List<Forum> forumList = new ArrayList<>();
                            for (Forums forums : data.sub) {
                                for (Forum forum : forums.data) {
                                    forum.setForumId(data.fid);
                                    forum.setCategoryName(forums.name);
                                    forum.setWeight(forums.weight);
                                    forumList.add(forum);
                                }
                            }
                            return Observable.just(forumList);
                        }
                    }

                }
                return null;
            }
        }).doOnNext(new Action1<List<Forum>>() {
            @Override
            public void call(List<Forum> fora) {
                saveToDb(fora);
            }
        }).onErrorReturn(new Func1<Throwable, List<Forum>>() {
            @Override
            public List<Forum> call(Throwable throwable) {
                return mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forumId)).list();
            }
        });

    }


    private void saveToDb(List<Forum> forums) {
        for (Forum forum : forums) {
            List<Forum> forumList = mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forum.getForumId()), ForumDao.Properties.Fid.eq(forum.getFid())).list();
            if (!forumList.isEmpty()) {
                forum.setId(forumList.get(0).getId());
            }
            mForumDao.insertOrReplace(forum);
        }
    }


    @Override
    public void detachView() {

    }
}
