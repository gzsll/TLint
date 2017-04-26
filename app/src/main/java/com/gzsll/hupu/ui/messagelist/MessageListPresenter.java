package com.gzsll.hupu.ui.messagelist;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.otto.MessageReadEvent;
import com.gzsll.hupu.util.ToastUtil;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sll on 2016/3/11.
 */
@PerActivity
public class MessageListPresenter implements MessageListContract.Presenter {

    private ForumApi mForumApi;
    private Bus mBus;

    private Subscription mSubscription;
    private MessageListContract.View mMessageListView;
    private String lastTid = "";
    private int page = 1;

    private List<Message> messages = new ArrayList<>();

    @Inject
    public MessageListPresenter(ForumApi mForumApi, Bus mBus) {
        this.mForumApi = mForumApi;
        this.mBus = mBus;
    }

    @Override
    public void onMessageListReceive() {
        mMessageListView.showLoading();
        loadMessageList(true);
    }

    private void loadMessageList(final boolean clear) {
        mSubscription = mForumApi.getMessageList(lastTid, page)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<MessageData>() {
                    @Override
                    public void call(MessageData messageData) {
                        if (clear) {
                            messages.clear();
                        }
                    }
                })
                .map(new Func1<MessageData, List<Message>>() {
                    @Override
                    public List<Message> call(MessageData messageData) {
                        if (messageData != null && messageData.status == 200) {
                            return addMessages(messageData.result.list);
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<List<Message>>() {
                    @Override
                    public void call(List<Message> messages) {
                        if (messages != null) {
                            if (messages.isEmpty()) {
                                mMessageListView.onEmpty();
                            } else {
                                mMessageListView.hideLoading();
                                mMessageListView.onRefreshCompleted();
                                mMessageListView.onLoadCompleted(true);
                                mMessageListView.renderMessageList(messages);
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
            mMessageListView.onError();
        } else {
            ToastUtil.showToast("数据加载失败，请重试");
            mMessageListView.hideLoading();
            mMessageListView.onRefreshCompleted();
            mMessageListView.onLoadCompleted(true);
        }
    }

    private List<Message> addMessages(List<Message> threadList) {
        for (Message thread : threadList) {
            if (!contains(thread)) {
                messages.add(thread);
            }
        }
        if (!messages.isEmpty()) {
            lastTid = messages.get(messages.size() - 1).tid;
        }
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

    @Override
    public void onRefresh() {
        lastTid = "";
        page = 1;
        onMessageListReceive();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onLoadMore() {
        page++;
        loadMessageList(false);
    }

    @Override
    public void onMessageClick(final Message message) {
        mMessageListView.showContentUi(message.tid, message.pid, Integer.valueOf(message.page));
        mForumApi.delMessage(message.id).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData baseData) {
                if (baseData != null && baseData.status == 200) {
                    mMessageListView.removeMessage(message);
                    mBus.post(new MessageReadEvent());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    @Override
    public void attachView(@NonNull MessageListContract.View view) {
        mMessageListView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mMessageListView = null;
    }
}
