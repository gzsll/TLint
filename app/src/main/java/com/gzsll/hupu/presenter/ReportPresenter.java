package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseResult;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.ReportView;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
public class ReportPresenter extends Presenter<ReportView> {

    @Inject
    ForumApi mForumApi;
    @Inject
    ToastHelper mToastHelper;


    @Singleton
    @Inject
    public ReportPresenter() {
    }


    public void submitReports(String tid, String pid, String type, String content) {
        view.showLoading();
        mForumApi.submitReports(tid, pid, type, content).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseResult>() {
            @Override
            public void call(BaseResult result) {
                view.hideLoading();
                if (result.status == 200) {
                    view.reportSuccess();
                    mToastHelper.showToast("举报成功~");
                } else {
                    mToastHelper.showToast("举报失败，请检查网络后重试");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                mToastHelper.showToast("举报失败，请检查网络后重试");
            }
        });
    }


    @Override
    public void detachView() {

    }
}
