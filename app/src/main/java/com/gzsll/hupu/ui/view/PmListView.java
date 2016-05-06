package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/6.
 */
public interface PmListView extends BaseView {
    void showLoading();

    void hideLoading();


    void renderPmList(List<Pm> pms);

    void onRefreshCompleted();

    void onLoadCompleted(boolean haMore);

    void onError();

    void onEmpty();
}
