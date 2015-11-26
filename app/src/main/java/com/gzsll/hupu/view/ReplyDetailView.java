package com.gzsll.hupu.view;

import com.gzsll.hupu.support.storage.bean.MiniReplyListItem;

import java.util.List;

/**
 * Created by sll on 2015/8/21.
 */
public interface ReplyDetailView extends BaseView {


    void showLoginView();

    void onMiniRepliesLoading();

    void loadMiniRepliesFinish();

    void renderMiniReplies(List<MiniReplyListItem> replyItems);


}
