package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.bean.PmDetailData;
import com.gzsll.hupu.bean.PmDetailResult;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.PmDetailView;

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
public class PmDetailPresenter extends Presenter<PmDetailView> {


    @Inject
    GameApi mGameApi;
    @Inject
    ToastHelper mToastHelper;

    @Inject
    @Singleton
    public PmDetailPresenter() {
    }

    private String lastMid = "";
    private List<PmDetail> mPmDetails = new ArrayList<>();
    private String uid;
    private Subscription mSubscription;

    public void onPmDetailReceive(String uid) {
        this.uid = uid;
        view.showLoading();
        loadPmDetail();
    }

    private void loadPmDetail() {
        mSubscription = mGameApi.queryPmDetail(lastMid, uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PmDetailData>() {
            @Override
            public void call(PmDetailData pmDetailData) {
                view.hideLoading();
                if (pmDetailData != null) {
                    PmDetailResult result = pmDetailData.result;
                    if (!result.data.isEmpty()) {
                        lastMid = result.data.get(0).pmid;
                        mPmDetails.addAll(0, result.data);
                        view.renderPmDetailList(mPmDetails);
                        view.onRefreshCompleted();
                    } else {
                        if (mPmDetails.isEmpty()) {
                            view.onEmpty();
                        } else {
                            mToastHelper.showToast("没有更多了");
                            view.onRefreshCompleted();
                        }
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (mPmDetails.isEmpty()) {
                    view.onError();
                } else {
                    mToastHelper.showToast("数据加载失败，请检查网络后重试");
                    view.hideLoading();
                }
            }
        });
    }

    public void onLoadMore() {
        loadPmDetail();
    }


    public void onReload() {
        onPmDetailReceive(uid);
    }


    @Override
    public void detachView() {
        lastMid = "";
        uid = "";
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
