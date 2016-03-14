package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/11.
 */
public interface ForumListView extends BaseView {

    void showLoading();

    void hideLoading();

    void onError();

    void renderForumList(List<Forum> forumList);
}
