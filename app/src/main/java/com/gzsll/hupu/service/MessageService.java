package com.gzsll.hupu.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.Logger;
import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.bean.MessageResult;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.injector.component.DaggerServiceComponent;
import com.gzsll.hupu.injector.module.ServiceModule;
import com.gzsll.hupu.ui.messagelist.MessageActivity;
import com.gzsll.hupu.ui.splash.SplashActivity;
import com.gzsll.hupu.util.NetWorkUtil;
import com.gzsll.hupu.util.SettingPrefUtil;

import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/16.
 */
public class MessageService extends Service {


    public static final String ACTION_GET = "com.gzsll.hupu.ACTION_GET";
    public static final String ACTION_UPDATE = "com.gzsll.hupu.ACTION_UPDATE";
    public static final String ACTION_CLOSE = "com.gzsll.hupu.ACTION_CLOSE";

    @Inject
    UserStorage mUserStorage;
    @Inject
    ForumApi mForumApi;

    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("服务初始化");
        DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mUserStorage.isLogin() || !SettingPrefUtil.getNotification(this)) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        String action = intent != null ? intent.getAction() : "";

        if (ACTION_GET.equals(action)) {
            resetTheTime();
            loadMessage();
        } else if (ACTION_UPDATE.equals(action)) {
            Logger.d("刷新时间");
            resetTheTime();
        } else if (ACTION_CLOSE.equals(action)) {
            clearAlarm();
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(getOperation());
    }

    private void resetTheTime() {
        Logger.d("resetTheTime");

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 指定时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR, 1);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getOperation());
    }

    private PendingIntent getOperation() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(ACTION_GET);
        PendingIntent sender =
                PendingIntent.getService(getBaseContext(), 1000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return sender;
    }

    private void loadMessage() {
        if (!NetWorkUtil.isWifiConnected(this)) {
            return;
        }
        mForumApi.getMessageList("", 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MessageData>() {
                    @Override
                    public void call(MessageData messageData) {
                        if (messageData != null && messageData.status == 200) {
                            notifyMessageCount(messageData.result);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public void notifyMessageCount(MessageResult result) {
        for (Message message : result.list) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_list_comment)
                    .setContentTitle(message.info)
                    .setContentText("来自TLint");
            Intent intent;
            if (AppManager.getAppManager().isAppExit()) {
                intent = new Intent(this, SplashActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(SplashActivity.ACTION_NOTIFICATION_MESSAGE);
            } else {
                intent = new Intent(this, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, Integer.valueOf(message.id), intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(contentIntent).setAutoCancel(true);
            builder.setTicker(message.info);

            notificationManager.notify(Integer.valueOf(message.id), builder.build());
        }
    }
}
