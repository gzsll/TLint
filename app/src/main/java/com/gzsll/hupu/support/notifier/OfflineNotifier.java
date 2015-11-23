package com.gzsll.hupu.support.notifier;

import android.app.Notification;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.gzsll.hupu.R;
import com.gzsll.hupu.support.db.Board;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/23.
 */
public class OfflineNotifier extends Notifier {

    @Inject
    Context mContext;


    public void notifyThreads(Board board, long offlineLength) {
        String title = String.format("离线板块[%s]", board.getBoardName());
        String content = String.format("节省流量%s", "");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineThreads, 0, builder);
    }

    public void notifyThreadsSuccess(int boardSize, int threadsSize, long threadsLength) {
        String title = String.format("%d个分组板块完成", boardSize);
        String content = String.format("共%d篇帖子，节省流量%s", threadsSize, "");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineThreads, 0, builder);
    }

    public void notifyPictureSuccess(int picSize, long picLength) {
        String title = "图片离线完成";
        String content = String.format("%s张图片，节省流量%s", String.valueOf(picSize), "");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        OfflineNotifier.this.notify(OfflinePicture, 0, builder);
    }

    public void notify(int request, int status, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notify(request, notification);
    }
}
