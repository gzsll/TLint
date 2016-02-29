package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.MessageReply;
import com.gzsll.hupu.view.MessageReplyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageReplyPresenter extends Presenter<MessageReplyView> {

    private String mLastId = "0";
    private List<MessageReply> mReplies = new ArrayList<>();

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

    private void addMessages(List<MessageReply> list) {
        for (MessageReply reply : list) {
            boolean contain = false;
            for (MessageReply messageReply : mReplies) {
                if (messageReply.getThreadInfo().getId() == reply.getThreadInfo().getId()) {
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
