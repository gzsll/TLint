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
import rx.functions.Func1;

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
        view.showLoading();
        loadMessageList(true);
    }

    private void loadMessageList(final boolean clear) {
        mForumApi.getMessageList(lastTid, page).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Action1<MessageData>() {
            @Override
            public void call(MessageData messageData) {
                if (clear) {
                    messages.clear();
                }
            }
        }).map(new Func1<MessageData, List<Message>>() {
            @Override
            public List<Message> call(MessageData messageData) {
                if (messageData != null && messageData.status == 200) {
                    return addMessages(messageData.result.list);
                }
                return null;
            }
        }).subscribe(new Action1<List<Message>>() {
            @Override
            public void call(List<Message> messages) {
                if (messages != null) {
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


    private List<Message> addMessages(List<Message> threadList) {
        for (Message thread : threadList) {
            if (!contains(thread)) {
                messages.add(thread);
            }
        }
        lastTid = messages.get(messages.size() - 1).tid;
        return messages;
    }

    private boolean contains(Message message) {
        boolean isContain = false;
        for (Message message1 : messages) {
            if (message.tid.equals(message1.tid)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    public void onRefresh() {
        lastTid = "";
        page = 1;
        onMessageListReceive();
    }

    public void onLoadMore() {
        page++;
        loadMessageList(false);
    }


    @Override
    public void detachView() {
        lastTid = "";
        page = 1;
        messages.clear();
    }
}
