package com.gzsll.hupu.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.UpdateInfo;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.ui.main.MainActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yalantis.phoenix.util.Logger;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2015/10/8.
 */
public class UpdateAgent {

    private OkHttpHelper mOkHttpHelper;
    private Activity mActivity;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Uri destinationUri;

    public static final String SDCARD_ROOT =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/gzsll/hupu";

    public UpdateAgent(OkHttpHelper mOkHttpHelper, Activity mActivity) {
        this.mOkHttpHelper = mOkHttpHelper;
        this.mActivity = mActivity;
    }

    public void checkUpdate() {
        this.checkUpdate(true);
    }

    public void checkUpdate(final boolean show) {
        mNotifyManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mActivity);
        Observable.create(new Observable.OnSubscribe<UpdateInfo>() {
            @Override
            public void call(Subscriber<? super UpdateInfo> subscriber) {
                try {
                    String result = mOkHttpHelper.getStringFromServer(Constants.UPDATE_URL);
                    subscriber.onNext(JSON.parseObject(result, UpdateInfo.class));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UpdateInfo>() {
                    @Override
                    public void call(UpdateInfo updateInfo) {
                        if (updateInfo != null) {
                            checkUpdateFinished(updateInfo, show);
                            if (updateInfo.extra != null) {
                                SettingPrefUtil.setNeedExam(mActivity, updateInfo.extra.needExam == 1);
                            }
                            SettingPrefUtil.setHuPuSign(mActivity, updateInfo.hupuSign);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void checkUpdateFinished(UpdateInfo updateInfo, boolean show) {
        if (updateInfo.versionCode > BuildConfig.VERSION_CODE && SettingPrefUtil.getAutoUpdate(
                mActivity) && show) {
            showUpdateDialog(updateInfo);
        }
    }

    private void showUpdateDialog(final UpdateInfo updateInfo) {
        if (updateInfo != null) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity).title("升级新版本");
            builder.positiveText("立刻升级").negativeText("取消").content(Html.fromHtml(updateInfo.updateInfo));
            builder.callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    try {
                        String url = updateInfo.updateUrl;
                        mBuilder.setContentTitle(mActivity.getString(R.string.app_name) + "正在更新")
                                .setAutoCancel(true)
                                .setSmallIcon(mActivity.getPackageManager()
                                        .getPackageInfo(mActivity.getPackageName(), 0).applicationInfo.icon);
                        destinationUri =
                                Uri.parse(SDCARD_ROOT + File.separator + FormatUtil.getFileNameFromUrl(url));
                        FileDownloader.getImpl()
                                .create(url)
                                .setPath(SDCARD_ROOT + File.separator + FormatUtil.getFileNameFromUrl(url))
                                .setListener(listener)
                                .start();
                        Toast.makeText(mActivity, "开始下载新版本，稍后会开始安装", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).show();
        }
    }

    private FileDownloadListener listener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            int progress = soFarBytes * 100 / totalBytes;
            String content = String.format("正在下载:%1$d%%", progress);
            mBuilder.setContentText(content).setProgress(totalBytes, soFarBytes, false);
            PendingIntent pendingintent =
                    PendingIntent.getActivity(mActivity, 0, new Intent(mActivity, MainActivity.class),
                            PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(pendingintent);
            mNotifyManager.notify(0, mBuilder.build());
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {

        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
            //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
            FileUtil.chmod("777", destinationUri.getPath());
            installAPKIntent.setDataAndType(Uri.parse("file://" + destinationUri.getPath()),
                    "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, 0, installAPKIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            installAPKIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(installAPKIntent);
            // 下载完成
            mBuilder.setContentText("下载成功（点击安装）").setProgress(0, 0, false);
            mNotifyManager.notify(0, mBuilder.build());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Logger.d("error:" + e.getMessage());
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };
}
