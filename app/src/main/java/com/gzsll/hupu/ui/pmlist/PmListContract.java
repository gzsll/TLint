package com.gzsll.hupu.ui.pmlist;

import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface PmListContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderPmList(List<Pm> pms);

        void onRefreshCompleted();

        void onLoadCompleted(boolean haMore);

        void onError();

        void onEmpty();

        void showPmDetailUi(String uid, String name);
    }

    interface Presenter extends BasePresenter<View> {
        void onPmListReceive();

        void onRefresh();

        void onReload();

        void onLoadMore();

        void onPmListClick(Pm pm);
    }
}
