package com.gzsll.hupu.ui.adapter;

import android.content.Context;

import com.gzsll.hupu.support.storage.bean.News;
import com.gzsll.hupu.ui.view.NewsListItem;
import com.gzsll.hupu.ui.view.NewsListItem_;

import javax.inject.Inject;

/**
 * Created by sll on 2015/10/10.
 */
public class NewsAdapter extends BaseListAdapter<News, NewsListItem> {
    @Inject
    public NewsAdapter() {
    }

    @Override
    protected NewsListItem onCreateItemView(Context context) {
        return NewsListItem_.build(mActivity);
    }

    @Override
    protected void onBindView(NewsListItem view, int position, News data) {
        view.init(data);
    }
}
