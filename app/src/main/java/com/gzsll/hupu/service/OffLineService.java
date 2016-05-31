package com.gzsll.hupu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.db.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sll on 2016/5/30.
 */
public class OffLineService extends Service {
  Logger logger = Logger.getLogger("OffLineService");
  public static final String START_DOWNLOAD = BuildConfig.APPLICATION_ID + ".action.START_DOWNLOAD";

  public static final int INIT = 0;
  public static final int PREPARE = 1;
  public static final int LOAD_THREAD = 2;
  public static final int LOAD_REPLY = 3;
  public static final int LOAD_PICTURE = 4;
  public static final int CANCEL = 5;
  public static final int FINISHED = 6;

  private int mCurrentStatus = INIT;

  private List<Forum> forums;
  private List<Forum> unOfflineForums;
  private int offlineThreadsCount;// 离线的帖子数量
  private int offlineRepliesCount; //离线的回复数量

  private long offlineThreadsLength = 0;// 离线的帖子总流量大小
  private long offlineRepliesLength = 0;
  private long offlinePictureLength = 0;// 离线的图片总流量大小

  private int offlinePictureCount = 0;// 离线的图片数量

  private LinkedBlockingQueue<Thread> threads = new LinkedBlockingQueue<>();// 线程安全队列

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent == null || TextUtils.isEmpty(intent.getAction())) {
      return super.onStartCommand(intent, flags, startId);
    }
    String action = intent.getAction();
    if (action.equals(START_DOWNLOAD)) {
      if (mCurrentStatus == INIT) {
        forums = (List<Forum>) intent.getSerializableExtra("boards");
        prepareOffline();
      } else {
        //ignore
        logger.debug("服务已启动，忽略请求");
      }
    }
    return super.onStartCommand(intent, flags, startId);
  }

  private void prepareOffline() {
    if (forums == null || forums.isEmpty()) {
      return;
    }
    mCurrentStatus = PREPARE;
    unOfflineForums = new ArrayList<>();
    unOfflineForums.addAll(forums);
    Observable.from(unOfflineForums).doOnNext(new Action1<Forum>() {
      @Override public void call(Forum forum) {

      }
    });
  }
}
