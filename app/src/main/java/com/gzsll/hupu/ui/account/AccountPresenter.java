package com.gzsll.hupu.ui.account;

import android.support.annotation.NonNull;

import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.injector.PerActivity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
@PerActivity
public class AccountPresenter implements AccountContract.Presenter {


    private UserDao mUserDao;
    private AccountContract.View mAccountView;


    @Inject
    public AccountPresenter(UserDao userDao) {
        mUserDao = userDao;
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
