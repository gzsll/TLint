package com.gzsll.hupu.ui.account;

import android.support.annotation.NonNull;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;

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
public class AccountPresenter implements AccountContract.Presenter {


    @Inject
    UserDao mUserDao;


    @Singleton
    @Inject
    public AccountPresenter() {

    }

    private AccountContract.View mAccountView;

    private void loadUserList() {
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                subscriber.onNext(mUserDao.queryBuilder().list());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                mAccountView.renderUserList(users);
            }
        });
    }

    @Override
    public void attachView(@NonNull AccountContract.View view) {
        mAccountView = view;
        loadUserList();
    }

    @Override
    public void detachView() {

    }
}
