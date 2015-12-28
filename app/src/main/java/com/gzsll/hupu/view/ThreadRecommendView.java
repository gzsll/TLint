package com.gzsll.hupu.view;

import com.gzsll.hupu.support.storage.bean.Thread;

/**
 * Created by sll on 2015/12/12.
 */
public interface ThreadRecommendView extends BaseListView<Thread> {

    void onRefreshing(boolean refresh);
}
