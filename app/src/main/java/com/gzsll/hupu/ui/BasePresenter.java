package com.gzsll.hupu.ui;

import android.support.annotation.NonNull;

/**
 * Created by sll on 2016/5/11.
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(@NonNull T view);

    void detachView();
}
