package com.gzsll.hupu.ui.splash;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.bean.UpdateInfo;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ChannelUtils;
import com.gzsll.hupu.util.SettingPrefUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/5/31.
 */
@PerActivity
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mSplashView;
    private Subscription mSubscription;

    private Context mContext;
    private OkHttpHelper mOkHttpHelper;

    @Inject
    public SplashPresenter(Context mContext, OkHttpHelper mOkHttpHelper) {
        this.mContext = mContext;
        this.mOkHttpHelper = mOkHttpHelper;
    }

    @Override
    public void initUmeng() {
        MobclickAgent.UMAnalyticsConfig config =
                new MobclickAgent.UMAnalyticsConfig(mContext, "55f1993be0f55a0fd9004fbc",
                        ChannelUtils.getChannel(mContext));
        MobclickAgent.startWithConfigure(config);
    }

    @Override
    public void initHuPuSign() {
        mSubscription = Observable.just(Constants.UPDATE_URL)
                .timeout(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, UpdateInfo>() {
                    @Override
                    public UpdateInfo call(String s) {
                        try {
                            String result = mOkHttpHelper.getStringFromServer(Constants.UPDATE_URL);
                            return JSON.parseObject(result, UpdateInfo.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UpdateInfo>() {
                    @Override
                    public void call(UpdateInfo updateInfo) {
                        if (updateInfo != null) {
                            if (updateInfo.extra != null) {
                                SettingPrefUtils.setNeedExam(mContext, updateInfo.extra.needExam == 1);
                            }
                            SettingPrefUtils.setHuPuSign(mContext, updateInfo.hupuSign);
                        }
                        mSplashView.showMainUi();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mSplashView.showMainUi();
                    }
                });
    }

    @Override
    public void attachView(@NonNull SplashContract.View view) {
        mSplashView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSplashView = null;
    }
}
