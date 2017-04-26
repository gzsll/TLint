package com.gzsll.hupu.components.notifier;

import android.app.Notification;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.gzsll.hupu.R;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.injector.PerService;
import com.gzsll.hupu.util.FormatUtil;

import javax.inject.Inject;

/**
 * Created by sll on 2016/6/2.
 */
@PerService
public class OfflineNotifier extends Notifier {

    private Context mContext;

    @Inject
    public OfflineNotifier(Context mContext) {
        this.mContext = mContext;
    }

    public void notifyThreads(Forum forum, long offlineLength) {
        String title = String.format("正在离线板块[%s]", forum.getName());
        String content = String.format("节省流量%s", FormatUtil.formatFileSize(offlineLength));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineThreads, 0, builder);
    }

    public void notifyThreadsSuccess(int forumSize, int threadsSize, long threadsLength) {
        String title = String.format("%d个板块完成", forumSize);
        String content =
                String.format("共%d篇帖子，节省流量%s", threadsSize, FormatUtil.formatFileSize(threadsLength));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineThreads, 0, builder);
    }

    public void notifyPictureSuccess(int picSize, long picLength) {
        String title = "图片离线完成";
        String content = String.format("%s张图片，节省流量%s", String.valueOf(picSize),
                FormatUtil.formatFileSize(picLength));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        OfflineNotifier.this.notify(OfflinePicture, 0, builder);
    }

    public void notifyReplies(Thread thread, long replyLength) {
        String title = String.format("正在离线[%s]的回复", thread.getTitle());
        String content = String.format("节省流量%s", FormatUtil.formatFileSize(replyLength));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineReplies, 0, builder);
    }

    public void notifyRepliesSuccess(int threadSize, int replySize, long replyLength) {
        String title = String.format("%d篇帖子完成", threadSize);
        String content =
                String.format("共%d篇回复，节省流量%s", replySize, FormatUtil.formatFileSize(replyLength));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        notify(OfflineReplies, 0, builder);
    }

    public void notify(int request, int status, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notify(request, notification);
    }
}
