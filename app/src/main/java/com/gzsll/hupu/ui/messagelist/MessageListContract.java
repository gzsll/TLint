package com.gzsll.hupu.ui.messagelist;

import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface MessageListContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderMessageList(List<Message> messages);

        void onRefreshCompleted();

        void onLoadCompleted(boolean haMore);

        void onError();

        void onEmpty();

        void showContentUi(String tid, String pid, int page);

        void removeMessage(Message message);
    }

    interface Presenter extends BasePresenter<View> {
        void onMessageListReceive();

        void onRefresh();

        void onReload();

        void onLoadMore();

        void onMessageClick(Message message);
    }
}
