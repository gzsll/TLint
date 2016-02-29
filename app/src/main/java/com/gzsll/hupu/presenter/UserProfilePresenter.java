package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.view.UserProfileView;

import javax.inject.Inject;

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
    ThreadApi mThreadApi;

    public void receiveUserInfo(String uid) {
        view.showLoading();

    }
}
