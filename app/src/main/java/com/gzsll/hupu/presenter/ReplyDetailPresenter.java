package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.view.ReplyDetailView;

import javax.inject.Inject;

/**
 * Created by sll on 2015/8/21.
 */
public class ReplyDetailPresenter extends Presenter<ReplyDetailView> {

    @Inject
    ThreadApi mThreadApi;


    public void onMiniReplyReceive(String groupThreadId, String groupReplyId, int page) {
        view.onMiniRepliesLoading();


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
