package com.gzsll.hupu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.com.google.gson.Gson;
import com.gzsll.hupu.bean.UpdateInfo;
import com.gzsll.hupu.helper.OkHttpHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2015/10/8.
 */
public class UpdateAgent {

    @Inject
    @Singleton
    public UpdateAgent() {
    }

    @Inject
    OkHttpHelper mOkHttpHelper;
    @Inject
    Gson mGson;


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Activity mActivity;

    private static final String UPDATE_URL = "http://www.pursll.com/update.json";

    public void checkUpdate(Activity mActivity) {
        this.mActivity = mActivity;
        Observable.just(UPDATE_URL).subscribeOn(Schedulers.io()).map(new Func1<String, UpdateInfo>() {
            @Override
            public UpdateInfo call(String s) {
                try {
                    String result = mOkHttpHelper.getStringFromServer(UPDATE_URL);
                    return mGson.fromJson(result, UpdateInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UpdateInfo>() {
            @Override
            public void call(UpdateInfo updateInfo) {
                checkUpdateFinished(updateInfo);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });


    }

    private void checkUpdateFinished(UpdateInfo updateInfo) {
        if (updateInfo != null && updateInfo.getVersionCode() > BuildConfig.VERSION_CODE) {
            showUpdateDialog(updateInfo);
        }
    }

    private void showUpdateDialog(final UpdateInfo updateInfo) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity).title("升级新版本");
                builder.positiveText("立刻升级").negativeText("取消").content(Html.fromHtml(updateInfo.getUpdateInfo()));
                builder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //TODO 在线下载
                        Uri uri = Uri.parse(updateInfo.getUpdateUrl());
                        Intent intent = new Intent(
                                "android.intent.action.VIEW", uri);
                        mActivity.startActivity(intent);

                    }
                }).show();
            }
        });


    }


}
