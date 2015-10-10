package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.support.storage.bean.UserResult;
import com.gzsll.hupu.view.UserProfileView;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/9/14.
 */
public class UserProfilePresenter extends Presenter<UserProfileView> {
    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Inject
    HuPuApi mHuPuApi;

    public void receiveUserInfo(String uid) {
        view.showLoading();
        mHuPuApi.getUserInfo(uid, new Callback<UserResult>() {
            @Override
            public void success(UserResult userResult, Response response) {
                view.hideLoading();
                if (userResult != null && userResult.getStatus() == 200) {
                    view.renderUserInfo(userResult.getData());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
