package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
public class MessageListPresenter extends Presenter<MessageListView> {


    @Singleton
    @Inject
    public MessageListPresenter() {
    }


    @Inject
    ForumApi mForumApi;
    @Inject
    ToastHelper mToastHelper;

    private String lastTid = "";
    private int page = 1;

    private List<Message> messages = new ArrayList<>();

    public void onMessageListReceive() {
        mForumApi.getMessageList(lastTid, page).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MessageData>() {
            @Override
            public void call(MessageData result) {

                if (result != null && result.status == 200) {
                    messages.addAll(result.result.list);
                    if (messages.isEmpty()) {
                        view.onEmpty();
                    } else {
                        view.hideLoading();
                        view.renderMessageList(messages);
                    }

                } else {
                    loadError();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loadError();
            }
        });
    }

    private void loadError() {
        if (messages.isEmpty()) {
            view.onError();
        } else {
            mToastHelper.showToast("数据加载失败，请重试");
            view.hideLoading();
        }
    }

    @Override
    public void detachView() {

    }
}
