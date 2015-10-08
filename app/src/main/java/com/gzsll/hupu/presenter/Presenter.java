package com.gzsll.hupu.presenter;


import com.gzsll.hupu.view.BaseView;

/**
 * Presenter contains the lifecycle of the view
 * <p/>
 * Created by sll on 2015/3/4.
 */
public abstract class Presenter<T extends BaseView> {

    protected T view;

    public void setView(T view) {
        this.view = view;
    }

    public abstract void initialize();

    public abstract void resume();

    public abstract void pause();

    public abstract void destroy();
}
