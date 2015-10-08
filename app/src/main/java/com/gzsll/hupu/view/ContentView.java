package com.gzsll.hupu.view;

import com.gzsll.hupu.storage.bean.ThreadReplyItems;

import java.util.List;
import java.util.Map;

/**
 * Created by sll on 2015/3/7.
 */
public interface ContentView extends BaseView {


    void renderContent(Map<Object, Object> map);

    void renderReplies(int page, int totalPage, List<ThreadReplyItems> replyItems);


    void reply(String title);

    void pm(String author);

    void onError(String error);

    void onEmpty();


}
