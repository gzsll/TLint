package com.gzsll.hupu.view;

import java.util.List;

/**
 * Created by sll on 2015/9/17.
 */
public interface BaseListView<T> extends BaseView {
    void renderList(List<T> list);

    void onError(String error);

    void onEmpty();

    void onScrollToTop();
}
