package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/6.
 */
public interface PmDetailView extends BaseView {
    void showLoading();

    void hideLoading();

    void isBlock(boolean isBlock);

    void renderPmDetailList(List<PmDetail> pmDetails);

    void scrollTo(int position);

    void onRefreshCompleted();

    void onError();

    void onEmpty();

    void cleanEditText();
}
