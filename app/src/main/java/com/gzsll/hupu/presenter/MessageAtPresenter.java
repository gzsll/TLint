package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.support.storage.bean.MessageAt;
import com.gzsll.hupu.support.storage.bean.MessageAtResult;
import com.gzsll.hupu.view.MessageAtView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/9/8.
 */
public class MessageAtPresenter extends Presenter<MessageAtView> {
    private String mLastId = "0";
    private List<MessageAt> mReplies = new ArrayList<>();

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
        mHuPuApi.getMessageAt(lastId, new Callback<MessageAtResult>() {
            @Override
            public void success(MessageAtResult messageAtResult, Response response) {
                if (messageAtResult != null && messageAtResult.getStatus() == 200) {
                    if (clear) {
                        mReplies.clear();
                        view.onScrollToTop();
                    }
                    mLastId = messageAtResult.getData().getLastId();
                    addMessages(messageAtResult.getData().getList());
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
