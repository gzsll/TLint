package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.bean.PmResult;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.PmListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/5/6.
 */
public class PmListPresenter extends Presenter<PmListView> {


    @Inject
    GameApi mGameApi;
    @Inject
    ToastHelper mToastHelper;

    @Inject
    @Singleton
    public PmListPresenter() {
    }

    private String lastTime = "";
    private List<Pm> mPms = new ArrayList<>();
    private boolean hasNextPage = true;
    private Subscription mSubscription;

    public void onPmListReceive() {
        view.showLoading();
        loadPmList(true);
    }

    private void loadPmList(final boolean clear) {
        mSubscription = mGameApi.queryPmList(lastTime).subscribeOn(Schedulers.io()).doOnNext(new Action1<PmData>() {
            @Override
            public void call(PmData pmData) {
                if (clear) {
                    mPms.clear();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PmData>() {
            @Override
            public void call(PmData pmData) {
                view.hideLoading();
                if (pmData != null) {
                    PmResult result = pmData.result;
                    hasNextPage = result.has_next_page.equals("1");
                    if (result.data.isEmpty()) {
                        if (mPms.isEmpty()) {
                            view.onEmpty();
                        } else {
                            mToastHelper.showToast("没有更多了");

                        }
                    } else {
                        addPmList(result.data);
                        view.renderPmList(mPms);
                    }
                    view.onRefreshCompleted();
                    view.onLoadCompleted(hasNextPage);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (mPms.isEmpty()) {
                    view.onError();
                } else {
                    mToastHelper.showToast("数据加载失败，请检查网络后重试");
                    view.hideLoading();
                    view.onRefreshCompleted();
                    view.onLoadCompleted(true);
                }
            }
        });
    }


    private void addPmList(List<Pm> pms) {
        for (Pm pm : pms) {
            boolean contain = false;
            for (Pm mPm : mPms) {
                if (pm.sid.equals(mPm.sid)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                mPms.add(pm);
            }
        }
        if (!mPms.isEmpty()) {
            lastTime = mPms.get(mPms.size() - 1).last_time;
        }
    }


    public void onRefresh() {
        lastTime = "";
        loadPmList(true);
    }

    public void onReload() {
        onPmListReceive();
    }


    public void onLoadMore() {
        if (!hasNextPage) {
            mToastHelper.showToast("没有更多了~");
            view.onLoadCompleted(false);
            return;
        }
        loadPmList(false);
    }


    @Override
    public void detachView() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mPms.clear();
        lastTime = "";
    }
}
