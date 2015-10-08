package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.storage.bean.MessageReply;
import com.gzsll.hupu.storage.bean.MessageReplyResult;
import com.gzsll.hupu.view.MessageReplyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageReplyPresenter extends Presenter<MessageReplyView> {

    private String mLastId = "0";
    private List<MessageReply> mReplies = new ArrayList<>();

    @Inject
    HuPuApi mHuPuApi;

    public void onRefresh() {
        view.onScrollToTop();
        loadMessageList("0", true);
    }


    public void onLoadMore() {
        loadMessageList(mLastId, false);
    }


    @Override
    public void initialize() {
        view.showLoading();
        loadMessageList(mLastId, true);
    }

    private void loadMessageList(String lastId, final boolean clear) {
        mHuPuApi.getMessageReply(lastId, new Callback<MessageReplyResult>() {
            @Override
            public void success(MessageReplyResult messageReplyResult, Response response) {
                if (messageReplyResult != null && messageReplyResult.getStatus() == 200) {
                    if (clear) {
                        mReplies.clear();
                        view.onScrollToTop();
                    }
                    mLastId = messageReplyResult.getData().getLastId();
                    addMessages(messageReplyResult.getData().getList());
                    view.renderList(mReplies);
                    view.hideLoading();
                } else {
                    if (mReplies.isEmpty()) {
                        view.onError("数据加载失败");
                    } else {
                        view.showToast("数据加载失败");
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                view.onError("数据加载失败");
            }
        });
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
