package com.gzsll.hupu.ui.userprofile;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.UserData;
import com.gzsll.hupu.bean.UserResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
public class UserProfilePresenter implements UserProfileContract.Presenter {


    @Inject
    GameApi mGameApi;


    private Subscription mSubscription;
    private UserProfileContract.View mUserProfileView;

    @Singleton
    @Inject
    public UserProfilePresenter() {
    }

    @Override
    public void receiveUserInfo(String uid) {
        mUserProfileView.showLoading();
        mSubscription = mGameApi.getUserInfo(uid).map(new Func1<UserData, UserResult>() {
            @Override
            public UserResult call(UserData userData) {
                if (userData != null) {
                    return userData.result;
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UserResult>() {
            @Override
            public void call(UserResult userResult) {
                mUserProfileView.hideLoading();
                if (userResult != null) {
                    mUserProfileView.renderUserData(userResult);
                } else {
                    mUserProfileView.showError();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mUserProfileView.hideLoading();
                mUserProfileView.showError();
            }
        });
    }


    @Override
    public void attachView(@NonNull UserProfileContract.View view) {
        mUserProfileView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
