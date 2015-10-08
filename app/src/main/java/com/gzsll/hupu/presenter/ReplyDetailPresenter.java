package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.storage.bean.ThreadReplyResult;
import com.gzsll.hupu.view.ReplyDetailView;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/8/21.
 */
public class ReplyDetailPresenter extends Presenter<ReplyDetailView> {

    @Inject
    HuPuApi mHuPuApi;


    public void onMiniReplyReceive(String groupThreadId, String groupReplyId, int page) {
        view.onMiniRepliesLoading();
        mHuPuApi.getMiniReplyList(groupThreadId, groupReplyId, page, new Callback<ThreadReplyResult>() {
            @Override
            public void success(ThreadReplyResult result, Response response) {
                view.loadMiniRepliesFinish();
                if (result != null && result.getStatus() == 200) {
                    view.renderMiniReplies(result.getData().getMiniReplyList().getLists());
                } else {
                    view.showToast("您的网络有问题，回复列表加载失败");
                }

            }

            @Override
            public void failure(RetrofitError error) {
                view.loadMiniRepliesFinish();
                view.showToast("您的网络有问题，回复列表加载失败");
            }
        });

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
