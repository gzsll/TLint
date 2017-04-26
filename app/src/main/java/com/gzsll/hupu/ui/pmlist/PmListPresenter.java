package com.gzsll.hupu.ui.pmlist;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.bean.PmResult;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/5/11.
 */
@PerActivity
public class PmListPresenter implements PmListContract.Presenter {

    private GameApi mGameApi;

    @Inject
    public PmListPresenter(GameApi gameApi) {
        mGameApi = gameApi;
    }

    private PmListContract.View mPmListView;
    private String lastTime = "";
    private List<Pm> mPms = new ArrayList<>();
    private boolean hasNextPage = true;
    private Subscription mSubscription;

    @Override
    public void onPmListReceive() {
        mPmListView.showLoading();
        loadPmList(true);
    }

    private void loadPmList(final boolean clear) {
        mSubscription =
                mGameApi.queryPmList(lastTime).subscribeOn(Schedulers.io()).doOnNext(new Action1<PmData>() {
                    @Override
                    public void call(PmData pmData) {
                        if (clear) {
                            mPms.clear();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PmData>() {
                    @Override
                    public void call(PmData pmData) {
                        mPmListView.hideLoading();
                        if (pmData != null) {
                            PmResult result = pmData.result;
                            hasNextPage = result.has_next_page.equals("1");
                            if (result.data.isEmpty()) {
                                if (mPms.isEmpty()) {
                                    mPmListView.onEmpty();
                                } else {
                                    ToastUtil.showToast("没有更多了");
                                }
                            } else {
                                addPmList(result.data);
                                mPmListView.renderPmList(mPms);
                            }
                            mPmListView.onRefreshCompleted();
                            mPmListView.onLoadCompleted(hasNextPage);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mPms.isEmpty()) {
                            mPmListView.onError();
                        } else {
                            ToastUtil.showToast("数据加载失败，请检查网络后重试");
                            mPmListView.hideLoading();
                            mPmListView.onRefreshCompleted();
                            mPmListView.onLoadCompleted(true);
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

    @Override
    public void onRefresh() {
        lastTime = "";
        loadPmList(true);
    }

    @Override
    public void onReload() {
        onPmListReceive();
    }

    @Override
    public void onLoadMore() {
        if (!hasNextPage) {
            ToastUtil.showToast("没有更多了~");
            mPmListView.onLoadCompleted(false);
            return;
        }
        loadPmList(false);
    }

    @Override
    public void onPmListClick(Pm pm) {
        mPmListView.showPmDetailUi(pm.puid, pm.nickname);
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mPmListView = null;
    }

    @Override
    public void attachView(@NonNull PmListContract.View view) {
        mPmListView = view;
    }
}
