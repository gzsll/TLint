package com.gzsll.hupu.presenter;

import android.support.annotation.NonNull;

import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/3/9.
 */
public abstract class Presenter<T extends BaseView> {

    protected T view;

    public void attachView(@NonNull T view) {
        this.view = view;
    }

    public abstract void detachView();
}
