package com.gzsll.hupu.ui.report;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.helper.ToastHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
public class ReportPresenter implements ReportContract.Presenter {

    @Inject
    ForumApi mForumApi;
    @Inject
    ToastHelper mToastHelper;


    @Singleton
    @Inject
    public ReportPresenter() {
    }

    private ReportContract.View mReportView;


    public void submitReports(String tid, String pid, String type, String content) {
        mReportView.showLoading();
        mForumApi.submitReports(tid, pid, type, content).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseData>() {
            @Override
            public void call(BaseData result) {
                mReportView.hideLoading();
                if (result.status == 200) {
                    mReportView.reportSuccess();
                    mToastHelper.showToast("举报成功~");
                } else {
                    mToastHelper.showToast("举报失败，请检查网络后重试");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mReportView.hideLoading();
                mToastHelper.showToast("举报失败，请检查网络后重试");
            }
        });
    }


    @Override
    public void attachView(@NonNull ReportContract.View view) {
        mReportView = view;
    }

    @Override
    public void detachView() {

    }
}
