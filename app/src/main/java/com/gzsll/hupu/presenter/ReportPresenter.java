package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.view.ReportView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2015/12/12.
 */
public class ReportPresenter extends Presenter<ReportView> {

    @Inject
    ThreadApi mThreadApi;


    public void submitReports(String tid, String pid, String type, String content) {
        view.showLoading();
        mThreadApi.submitReports(tid, pid, type, content).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseResult>() {
            @Override
            public void call(BaseResult result) {
                view.hideLoading();
                if (result.getStatus() == 200) {
                    view.onReportSuccess();
                    view.showToast("举报成功~");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                view.showToast("举报失败，请检查网络后重试");
            }
        });
    }


    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
