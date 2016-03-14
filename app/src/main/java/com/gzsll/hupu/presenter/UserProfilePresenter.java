package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.UserData;
import com.gzsll.hupu.bean.UserResult;
import com.gzsll.hupu.ui.view.UserProfileView;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
public class UserProfilePresenter extends Presenter<UserProfileView> {


    @Inject
    GameApi mGameApi;


    private Subscription mSubscription;

    @Singleton
    @Inject
    public UserProfilePresenter() {
    }


    public void receiveUserInfo(String uid) {
        view.showLoading();
        mSubscription = mGameApi.getUserInfo(uid).map(new Func1<UserResult, UserData>() {
            @Override
            public UserData call(UserResult userResult) {
                if (userResult != null) {
                    return userResult.result;
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UserData>() {
            @Override
            public void call(UserData userData) {
                view.hideLoading();
                if (userData != null) {
                    view.renderUserData(userData);
                } else {
                    view.showError();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                view.showError();
            }
        });
    }


    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
