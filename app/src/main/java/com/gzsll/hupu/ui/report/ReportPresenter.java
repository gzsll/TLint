package com.gzsll.hupu.ui.report;

import android.support.annotation.NonNull;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ToastUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
@PerActivity
public class ReportPresenter implements ReportContract.Presenter {

    private ForumApi mForumApi;

    @Inject
    public ReportPresenter(ForumApi forumApi) {
        mForumApi = forumApi;
    }

    private Subscription mSubscription;
    private ReportContract.View mReportView;

    public void submitReports(String tid, String pid, String type, String content) {
        mReportView.showLoading();
        mSubscription = mForumApi.submitReports(tid, pid, type, content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData result) {
                        mReportView.hideLoading();
                        if (result.status == 200) {
                            mReportView.reportSuccess();
                            ToastUtil.showToast("举报成功~");
                        } else {
                            ToastUtil.showToast("举报失败，请检查网络后重试");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mReportView.hideLoading();
                        ToastUtil.showToast("举报失败，请检查网络后重试");
                    }
                });
    }

    @Override
    public void attachView(@NonNull ReportContract.View view) {
        mReportView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mReportView = null;
    }
}
