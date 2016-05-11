package com.gzsll.hupu.ui.pmdetail;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.bean.PmDetailData;
import com.gzsll.hupu.bean.PmDetailResult;
import com.gzsll.hupu.bean.PmSettingData;
import com.gzsll.hupu.bean.SendPm;
import com.gzsll.hupu.bean.SendPmData;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.ToastHelper;

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
public class PmDetailPresenter implements PmDetailContract.Presenter {


    @Inject
    GameApi mGameApi;
    @Inject
    ToastHelper mToastHelper;
    @Inject
    UserStorage mUserStorage;

    @Inject
    @Singleton
    public PmDetailPresenter() {
    }


    private PmDetailContract.View mPmDetailView;
    private String lastMid = "";
    private List<PmDetail> mPmDetails = new ArrayList<>();
    private String uid;
    private Subscription mSubscription;
    private boolean isBlock;


    @Override
    public void attachView(@NonNull PmDetailContract.View view) {
        mPmDetailView = view;
        mGameApi.queryPmSetting(uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PmSettingData>() {
            @Override
            public void call(PmSettingData pmSettingData) {
                if (pmSettingData != null) {
                    isBlock = pmSettingData.result.is_block == 1;
                    mPmDetailView.isBlock(isBlock);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    @Override
    public void bind(String uid) {
        this.uid = uid;
    }

    @Override
    public void onPmDetailReceive() {
        mPmDetailView.showLoading();
        loadPmDetail();
    }

    private void loadPmDetail() {
        mSubscription = mGameApi.queryPmDetail(lastMid, uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PmDetailData>() {
            @Override
            public void call(PmDetailData pmDetailData) {
                mPmDetailView.hideLoading();
                if (pmDetailData != null) {
                    PmDetailResult result = pmDetailData.result;
                    if (!result.data.isEmpty()) {
                        lastMid = result.data.get(0).pmid;
                        mPmDetails.addAll(0, result.data);
                        mPmDetailView.renderPmDetailList(mPmDetails);
                        mPmDetailView.scrollTo(result.data.size() - 1);
                        mPmDetailView.onRefreshCompleted();
                    } else {
                        if (mPmDetails.isEmpty()) {
                            mPmDetailView.onEmpty();
                        } else {
                            mToastHelper.showToast("没有更多了");
                            mPmDetailView.onRefreshCompleted();
                        }
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (mPmDetails.isEmpty()) {
                    mPmDetailView.onError();
                } else {
                    mToastHelper.showToast("数据加载失败，请检查网络后重试");
                    mPmDetailView.hideLoading();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        loadPmDetail();
    }

    @Override
    public void onReload() {
        onPmDetailReceive();
    }

    @Override
    public void send(String content) {
        if (TextUtils.isEmpty(content)) {
            mToastHelper.showToast("内容不能为空");
            return;
        }
        mGameApi.pm(content, uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SendPmData>() {
            @Override
            public void call(SendPmData sendPmData) {
                if (sendPmData != null) {
                    SendPm pm = sendPmData.result;
                    if (pm.code.equals("0")) {
                        PmDetail detail = new PmDetail();
                        detail.puid = mUserStorage.getUid();
                        detail.header = mUserStorage.getUser().getIcon();
                        detail.content = pm.content;
                        detail.pmid = pm.pmid;
                        detail.create_time = pm.create_time;
                        mPmDetails.add(detail);
                        mPmDetailView.renderPmDetailList(mPmDetails);
                        mPmDetailView.scrollTo(mPmDetails.size() - 1);
                        mPmDetailView.onRefreshCompleted();
                        mToastHelper.showToast("发送成功");
                        mPmDetailView.cleanEditText();
                    } else {
                        mToastHelper.showToast(sendPmData.result.desc);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("发送失败，请检查您的网络后重试");
            }
        });
    }

    @Override
    public void clear() {
        mGameApi.clearPm(uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData baseData) {
                mToastHelper.showToast("清空记录成功");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast("清空记录失败，请检查网络后重试");
            }
        });
    }

    @Override
    public void block() {
        mGameApi.blockPm(uid, isBlock ? 0 : 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData baseData) {
                mToastHelper.showToast(isBlock ? "取消屏蔽成功" : "屏蔽成功");
                isBlock = !isBlock;
                mPmDetailView.isBlock(isBlock);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mToastHelper.showToast(isBlock ? "取消屏蔽失败，请检查网络后重试" : "屏蔽失败，请检查网络后重试");
            }
        });
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
