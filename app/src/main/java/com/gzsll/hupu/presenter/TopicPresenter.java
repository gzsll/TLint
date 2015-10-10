package com.gzsll.hupu.presenter;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.Topic;
import com.gzsll.hupu.support.storage.bean.TopicResult;
import com.gzsll.hupu.view.TopicView;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/5/28.
 */
public class TopicPresenter extends Presenter<TopicView> {


    private int page = 1;


    @Inject
    ThreadApi mThreadApi;
    @Inject
    Bus bus;


    private List<Topic> topics = new ArrayList<Topic>();
    private int type;
    private String uid;

    private void load(int type, String uid, final boolean isClear) {
        if (type == Constants.NAV_TOPIC_LIST) {
            mThreadApi.getUserThreadList(page, uid, new Callback<TopicResult>() {
                @Override
                public void success(TopicResult topicResult, Response response) {
                    loadFinish(topicResult, isClear);
                }

                @Override
                public void failure(RetrofitError error) {
                    view.onError(error.getMessage());
                }
            });
        } else {
            mThreadApi.getUserThreadFavoriteList(page, uid, new Callback<TopicResult>() {
                @Override
                public void success(TopicResult topicResult, Response response) {
                    loadFinish(topicResult, isClear);
                }

                @Override
                public void failure(RetrofitError error) {
                    view.onError(error.getMessage());
                }
            });
        }
    }


    private void loadFinish(TopicResult result, boolean isClear) {
        if (isClear) {
            topics.clear();
        }
        if (result.getStatus() == 200) {
            addTopics(result.getData().getList());
            if (topics.isEmpty()) {
                view.onEmpty();
            } else {
                view.renderList(topics);
                view.hideLoading();
            }
        } else {
            if (topics.isEmpty()) {
                view.onError("数据加载失败");
            } else {
                view.showToast("数据加载失败");
            }
        }
    }

    private void addTopics(List<Topic> topicList) {
        for (Topic topic : topicList) {
            boolean isContain = false;
            for (Topic topic1 : topics) {
                if (topic.getId() == topic1.getId()) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                topics.add(topic);
            }
        }
    }


    public void onTopicReceive(int type, String uid) {
        view.showLoading();
        this.type = type;
        this.uid = uid;
        page = 1;
        load(type, uid, true);
    }


    public void onRefresh() {
        view.onScrollToTop();
        page = 1;
        load(type, uid, true);

    }

    public void onLoadMore() {
        page++;
        load(type, uid, false);
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
