package com.gzsll.hupu.ui.forum;

import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface ForumListContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void onError();

        void renderForumList(List<Forum> forumList);

        void showThreadUi(String fid);

        void removeForum(Forum forum);
    }

    interface Presenter extends BasePresenter<View> {

        void onForumListReceive(String forumId);

        void onForumAttentionDelClick(Forum forum);

        void onForumOfflineClick(Forum forum);

        void onForumClick(Forum forum);
    }
}
