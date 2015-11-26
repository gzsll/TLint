package com.gzsll.hupu.presenter;

import com.gzsll.hupu.support.utils.FormatHelper;
import com.gzsll.hupu.support.utils.OkHttpHelper;
import com.gzsll.hupu.view.ThreadListView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/1.
 */
public class SearchPresenter extends Presenter<ThreadListView> {

    Logger logger = Logger.getLogger(SearchPresenter.class.getSimpleName());

    private int pageIndex = 1;
    private int mFid;
    private String key;

    @Inject
    FormatHelper mFormatHelper;
    @Inject
    OkHttpHelper mOkHttpHelper;


    @Inject
    Bus bus;

    public void onItemSelected(int position) {

        // bus.post(new ThreadSelectedEvent(mFid, forum.getTid()));
    }


    public void onLoadMore() {
        pageIndex++;

    }


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
}
