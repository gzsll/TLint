package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/11.
 */
public interface MessageListView extends BaseView {

    void showLoading();

    void hideLoading();

    void renderMessageList(List<Message> messages);

    void onRefreshCompleted();

    void onLoadCompleted(boolean haMore);

    void onError();

    void onEmpty();

}
