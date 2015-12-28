package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.view.ReportView;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/12/12.
 */
public class ReportPresenter extends Presenter<ReportView> {

    @Inject
    ThreadApi mThreadApi;


    public void submitReports(String tid, String pid, String type, String content) {
        view.showLoading();
        mThreadApi.submitReports(tid, pid, type, content, new Callback<BaseResult>() {
            @Override
            public void success(BaseResult result, Response response) {
                view.hideLoading();
                view.showToast("举报成功~");
                if (result.getStatus() == 200) {
                    view.onReportSuccess();
                }
            }

            @Override
            public void failure(RetrofitError error) {
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
