package com.gzsll.hupu.ui.forum;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Forums;
import com.gzsll.hupu.bean.ForumsData;
import com.gzsll.hupu.bean.ForumsResult;
import com.gzsll.hupu.bean.MyForumsData;
import com.gzsll.hupu.bean.MyForumsResult;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.injector.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

/**
 * Created by sll on 2016/3/11.
 */
@PerActivity
public class ForumListPresenter implements ForumListContract.Presenter {


    private ForumDao mForumDao;
    private ForumApi mForumApi;


    private ForumListContract.View mForumListView;
    private boolean isFirst = true;
    private PublishSubject<List<Forum>> mSubject;


    @Inject
    public ForumListPresenter(ForumDao forumDao, ForumApi forumApi) {
        mForumDao = forumDao;
        mForumApi = forumApi;
        mSubject = PublishSubject.create();
    }


    @Override
    public void onForumListReceive(final String forumId) {
        mForumListView.showLoading();
        getForumListObservable(forumId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Forum>>() {
            @Override
            public void call(List<Forum> fora) {
                if (fora == null || fora.isEmpty()) {
                    if (!isFirst) {
                        mForumListView.onError();
                    }
                    isFirst = false;
                } else {
                    mForumListView.hideLoading();
                    mForumListView.renderForumList(fora);
                }
            }
        });

        getForumObservable(forumId);
    }

    private Observable<List<Forum>> getForumListObservable(final String forumId) {
        Observable<List<Forum>> firstObservable = Observable.fromCallable(new Func0<List<Forum>>() {
            @Override
            public List<Forum> call() {
                return mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forumId)).list();
            }
        });
        return firstObservable.concatWith(mSubject);
    }


    private void getForumObservable(String forumId) {
        if (TextUtils.equals(forumId, "0")) {
            loadUserForums();
        } else {
            loadAllForums(forumId);
        }
    }

    private void loadUserForums() {
        mForumApi.getMyForums().doOnNext(new Action1<MyForumsData>() {
            @Override
            public void call(MyForumsData result) {
                if (result != null && result.data != null) {
                    MyForumsResult data = result.data;
                    for (Forum forum : data.sub) {
                        forum.setForumId(data.fid);
                        forum.setCategoryName(data.name);
                        forum.setWeight(1);
                    }
                    saveToDb(data.sub, "0", true);
                }
            }
        }).subscribe(new Action1<MyForumsData>() {
            @Override
            public void call(MyForumsData myForumsData) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                saveToDb(null, "0", true);
            }
        });
    }

    private void loadAllForums(final String forumId) {
        mForumApi.getForums().doOnNext(new Action1<ForumsData>() {
            @Override
            public void call(ForumsData result) {
                if (result != null) {
                    for (ForumsResult data : result.data) {
                        List<Forum> forumList = new ArrayList<>();
                        for (Forums forums : data.sub) {
                            for (Forum forum : forums.data) {
                                forum.setForumId(data.fid);
                                forum.setCategoryName(forums.name);
                                forum.setWeight(forums.weight);
                                forumList.add(forum);
                            }

                        }
                        saveToDb(forumList, data.fid, data.fid.equals(forumId));
                    }

                }
            }
        }).subscribe(new Action1<ForumsData>() {
            @Override
            public void call(ForumsData forumsData) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                saveToDb(null, forumId, true);
            }
        });

    }


    private void saveToDb(List<Forum> forums, String forumId, boolean concat) {
        if (forums != null && !forums.isEmpty()) {
            mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forumId)).buildDelete().executeDeleteWithoutDetachingEntities();
            for (Forum forum : forums) {
                mForumDao.insertOrReplace(forum);
            }
        }
        if (concat) {
            mSubject.onNext(mForumDao.queryBuilder().where(ForumDao.Properties.ForumId.eq(forumId)).list());
        }
    }


    @Override
    public void attachView(@NonNull ForumListContract.View view) {
        mForumListView = view;
    }

    @Override
    public void detachView() {

    }
}
