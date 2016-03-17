package com.gzsll.hupu.presenter;

import android.support.annotation.NonNull;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.ui.view.AccountView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
public class AccountPresenter extends Presenter<AccountView> {


    @Inject
    UserDao mUserDao;


    @Singleton
    @Inject
    public AccountPresenter() {

    }

    @Override
    public void attachView(@NonNull AccountView view) {
        super.attachView(view);
        loadUserList();
    }

    private void loadUserList() {
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                subscriber.onNext(mUserDao.queryBuilder().list());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                view.renderUserList(users);
            }
        });
    }

    @Override
    public void detachView() {

    }
}
