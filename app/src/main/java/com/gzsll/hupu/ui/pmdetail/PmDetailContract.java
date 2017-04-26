package com.gzsll.hupu.ui.pmdetail;

import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface PmDetailContract {

    interface View extends BaseView {
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

    interface Presenter extends BasePresenter<View> {

        void onPmDetailReceive();

        void onLoadMore();

        void onReload();

        void send(String content);

        void clear();

        void block();
    }
}
