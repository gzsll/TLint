package com.gzsll.hupu.ui.account;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
@PerActivity
public class AccountPresenter implements AccountContract.Presenter {

    private UserDao mUserDao;
    private Activity mActivity;
    private UserStorage mUserStorage;
    private Bus mBus;

    private AccountContract.View mAccountView;
    private Subscription mSubscription;

    @Inject
    public AccountPresenter(UserDao mUserDao, Activity mActivity, UserStorage mUserStorage,
                            Bus mBus) {
        this.mUserDao = mUserDao;
        this.mActivity = mActivity;
        this.mUserStorage = mUserStorage;
        this.mBus = mBus;
    }

    private void loadUserList() {
        mSubscription = Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                subscriber.onNext(mUserDao.queryBuilder().list());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
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
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mAccountView = null;
    }

    @Override
    public void onAccountDelClick(final User user) {
        new MaterialDialog.Builder(mActivity).title("提示")
                .content("确认删除账号?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mUserDao.delete(user);
                        if (String.valueOf(user.getUid()).equals(mUserStorage.getUid())) {
                            mUserStorage.logout();
                        }
                        mBus.post(new AccountChangeEvent());
                        loadUserList();
                    }
                })
                .show();
    }

    @Override
    public void onAccountClick(User user) {
        if (!String.valueOf(user.getUid()).equals(mUserStorage.getUid())) {
            mUserStorage.login(user);
            mBus.post(new AccountChangeEvent());
            mActivity.finish();
        }
    }
}
