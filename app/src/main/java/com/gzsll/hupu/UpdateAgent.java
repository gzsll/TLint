package com.gzsll.hupu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.gzsll.hupu.storage.bean.UpdateInfo;
import com.gzsll.hupu.utils.OkHttpHelper;
import com.jockeyjs.util.BackgroundExecutor;

import javax.inject.Inject;

/**
 * Created by sll on 2015/10/8.
 */
public class UpdateAgent {

    @Inject
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
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = mOkHttpHelper.getStringFromServer(UPDATE_URL);
                    UpdateInfo updateInfo = mGson.fromJson(result, UpdateInfo.class);
                    checkUpdateFinished(updateInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void checkUpdateFinished(UpdateInfo updateInfo) {
        if (updateInfo.getVersionCode() > BuildConfig.VERSION_CODE) {
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
