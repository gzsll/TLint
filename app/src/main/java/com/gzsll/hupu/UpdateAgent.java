package com.gzsll.hupu;

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
import com.gzsll.hupu.bean.UpdateInfo;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.FormatHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.ui.activity.MainActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yalantis.phoenix.util.Logger;

import java.io.File;

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
    FormatHelper mFormatHelper;
    @Inject
    FileHelper mFileHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Uri destinationUri;

    public static final String SDCARD_ROOT =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/gzsll/hupu";

    private Activity mActivity;

    private static final String UPDATE_URL = "http://www.pursll.com/update.json";

    public void checkUpdate(Activity mActivity) {
        this.mActivity = mActivity;
        mNotifyManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mActivity);
        Observable.just(UPDATE_URL).subscribeOn(Schedulers.io()).map(new Func1<String, UpdateInfo>() {
            @Override
            public UpdateInfo call(String s) {
                try {
                    String result = mOkHttpHelper.getStringFromServer(UPDATE_URL);
                    return JSON.parseObject(result, UpdateInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UpdateInfo>() {
            @Override
            public void call(UpdateInfo updateInfo) {
                checkUpdateFinished(updateInfo);
                if (updateInfo != null && updateInfo.extra != null) {
                    mSettingPrefHelper.setNeedExam(updateInfo.extra.needExam == 1);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });


    }

    private void checkUpdateFinished(UpdateInfo updateInfo) {
        if (updateInfo != null && updateInfo.versionCode > BuildConfig.VERSION_CODE) {
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
                        mBuilder.setContentTitle(mActivity.getString(R.string.app_name) + "正在更新").setAutoCancel(true).setSmallIcon(mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).applicationInfo.icon);
                        destinationUri = Uri.parse(SDCARD_ROOT + File.separator + mFormatHelper.getFileNameFromUrl(url));
                        FileDownloader.getImpl().create(url).setPath(SDCARD_ROOT + File.separator + mFormatHelper.getFileNameFromUrl(url)).setListener(listener).start();
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
            PendingIntent pendingintent = PendingIntent.getActivity(mActivity, 0, new Intent(mActivity, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
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
            mFileHelper.chmod("777", destinationUri.getPath());
            installAPKIntent.setDataAndType(Uri.parse("file://" + destinationUri.getPath()), "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, 0, installAPKIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
