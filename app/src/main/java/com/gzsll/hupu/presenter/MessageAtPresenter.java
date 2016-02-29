package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.MessageAt;
import com.gzsll.hupu.view.MessageAtView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8.
 */
public class MessageAtPresenter extends Presenter<MessageAtView> {
    private String mLastId = "0";
    private List<MessageAt> mReplies = new ArrayList<>();

    @Inject
    ThreadApi mThreadApi;

    public void onRefresh() {
        view.onScrollToTop();
        loadMessageList("0", true);
    }


    public void onLoadMore() {
        loadMessageList(mLastId, false);
    }

    public void onReload() {
        loadMessageList(mLastId, false);
    }


    @Override
    public void initialize() {
        view.showLoading();
        loadMessageList(mLastId, true);
    }

    private void loadMessageList(String lastId, final boolean clear) {

    }

    private void addMessages(List<MessageAt> list) {
        for (MessageAt reply : list) {
            boolean contain = false;
            for (MessageAt messageAt : mReplies) {
                if (messageAt.getId() == reply.getId()) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                mReplies.add(reply);
            }
        }
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
