package com.gzsll.hupu.ui.content;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.data.ContentRepository;
import com.gzsll.hupu.db.ImageCache;
import com.gzsll.hupu.db.ImageCacheDao;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.otto.UpdateContentPageEvent;
import com.gzsll.hupu.util.ConfigUtils;
import com.gzsll.hupu.util.FileUtils;
import com.gzsll.hupu.util.SecurityUtils;
import com.gzsll.hupu.util.ToastUtils;
import com.squareup.otto.Bus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/5/25.
 */
public class ContentPagerPresenter implements ContentPagerContract.Presenter {

  private static final Logger logger =
      Logger.getLogger(ContentPagerPresenter.class.getSimpleName());

  private ContentRepository mContentRepository;
  private ForumApi mForumApi;
  private Bus mBus;
  private Context mContext;
  private ImageCacheDao mImageCacheDao;
  private OkHttpHelper mOkHttpHelper;

  private ContentPagerContract.View mContentView;

  private Subscription mInfoSubscription;
  private Subscription mLightSubscription;
  private Subscription mReplySubscription;

  private ConcurrentHashMap<String, String> imageMap = new ConcurrentHashMap<>();
  //存放图片下载器信息
  private List<String> taskArray = new ArrayList<>();

  private List<ThreadReply> lightReplies = new ArrayList<>();
  private List<ThreadReply> replies = new ArrayList<>();
  private String fid;
  private String tid;

  @Inject
  public ContentPagerPresenter(ContentRepository mContentRepository, ForumApi mForumApi, Bus mBus,
      Context mContext, ImageCacheDao mImageCahceDao, OkHttpHelper mOkHttpHelper) {
    this.mContentRepository = mContentRepository;
    this.mForumApi = mForumApi;
    this.mBus = mBus;
    this.mContext = mContext;
    this.mImageCacheDao = mImageCahceDao;
    this.mOkHttpHelper = mOkHttpHelper;
  }

  @Override
  public void onThreadInfoReceive(final String tid, final String fid, String pid, final int page) {
    this.fid = fid;
    this.tid = tid;
    if (page == 1) {
      mInfoSubscription = mContentRepository.getThreadInfo(fid, tid)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<ThreadInfo>() {
            @Override public void call(ThreadInfo threadInfo) {
              mContentView.sendMessageToJS("addThreadInfo", threadInfo);
              loadLightReplies(tid, fid);
              loadReplies(tid, fid, page);
              mContentView.hideLoading();
              mBus.post(
                  new UpdateContentPageEvent(threadInfo.getPage(), threadInfo.getTotalPage()));
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
              throwable.printStackTrace();
              mContentView.onError();
            }
          });
    } else {
      loadReplies(tid, fid, page);
    }
  }

  @Override public void onReply(int area, int index) {
    ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
    mContentView.showReplyUi(fid, tid, reply.getPid(), reply.getContent());
  }

  @Override public void onReport(int area, int index) {
    ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
    mContentView.showReportUi(tid, reply.getPid());
  }

  @Override public void addLight(int area, int index) {
    final ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
    mForumApi.addLight(tid, fid, reply.getPid())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<BaseData>() {
          @Override public void call(BaseData baseData) {
            if (baseData != null) {
              if (baseData.status == 200) {
                AddLight light = new AddLight(baseData.result, reply.getPid());
                mContentView.sendMessageToJS("addLight", light);
                ToastUtils.showToast("点亮成功");
              } else if (baseData.error != null) {
                ToastUtils.showToast(baseData.error.text);
              }
            } else {
              ToastUtils.showToast("点亮失败，请检查网络后重试");
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            ToastUtils.showToast("点亮失败，请检查网络后重试");
          }
        });
  }

  @Override public void addRuLight(int area, int index) {
    final ThreadReply reply = area == 0 ? lightReplies.get(index) : replies.get(index);
    mForumApi.addRuLight(tid, fid, reply.getPid())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<BaseData>() {
          @Override public void call(BaseData baseData) {
            if (baseData != null) {
              if (baseData.status == 200) {
                AddLight light = new AddLight(baseData.result, reply.getPid());
                mContentView.sendMessageToJS("addLight", light);
                ToastUtils.showToast("点灭成功");
              } else if (baseData.error != null) {
                ToastUtils.showToast(baseData.error.text);
              }
            } else {
              ToastUtils.showToast("点灭失败，请检查网络后重试");
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            ToastUtils.showToast("点灭失败，请检查网络后重试");
          }
        });
  }

  @Override public HupuBridge getJavaScriptInterface() {
    return new HupuBridge();
  }

  public class AddLight {

    public int light;
    public String pid;

    public AddLight(int light, String pid) {
      this.light = light;
      this.pid = pid;
    }
  }

  private void loadLightReplies(String tid, String fid) {
    mLightSubscription = mContentRepository.getLightReplies(fid, tid)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<ThreadReply>>() {
          @Override public void call(List<ThreadReply> threadReplies) {
            lightReplies = threadReplies;
            if (!threadReplies.isEmpty()) {
              mContentView.sendMessageToJS("addLightTitle", "\"这些回帖亮了\"");
              for (int i = 0; i < threadReplies.size(); i++) {
                ThreadReply reply = threadReplies.get(i);
                reply.setIndex(i);
                mContentView.sendMessageToJS("addLightPost", reply);
              }
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            throwable.printStackTrace();
            mContentView.onError();
          }
        });
  }

  private void loadReplies(String tid, String fid, final int page) {
    mReplySubscription = mContentRepository.getReplies(fid, tid, page)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<ThreadReply>>() {
          @Override public void call(List<ThreadReply> threadReplies) {
            replies = threadReplies;
            if (page == 1 && !threadReplies.isEmpty()) {
              mContentView.sendMessageToJS("addReplyTitle", "\"全部回帖\"");
            }
            for (int i = 0; i < threadReplies.size(); i++) {
              ThreadReply reply = threadReplies.get(i);
              reply.setIndex(i);
              mContentView.sendMessageToJS("addReply", reply);
            }
            mContentView.hideLoading();
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            throwable.printStackTrace();
            mContentView.onError();
          }
        });
  }

  @Override public void attachView(@NonNull ContentPagerContract.View view) {
    mContentView = view;
    mContentView.showLoading();
    //Observable.create(new Observable.OnSubscribe<String>() {
    //  @Override public void call(Subscriber<? super String> subscriber) {
    //    subscriber.onNext(FileUtils.stringFromAssetsFile(mContext, "hupu_thread.html"));
    //  }
    //})
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Action1<String>() {
    //      @Override public void call(String s) {
    //        mContentView.loadDataWithBaseUrl(s);
    //      }
    //    });
  }

  @Override public void detachView() {
    if (mInfoSubscription != null && !mInfoSubscription.isUnsubscribed()) {
      mInfoSubscription.unsubscribe();
    }
    if (mLightSubscription != null && !mLightSubscription.isUnsubscribed()) {
      mLightSubscription.unsubscribe();
    }
    if (mReplySubscription != null && !mReplySubscription.isUnsubscribed()) {
      mReplySubscription.unsubscribe();
    }
    mContentView = null;
  }

  public class HupuBridge {

    @JavascriptInterface public String replaceImage(String imageUrl, int index) {

      if (imageMap.contains(imageUrl)) {
        return imageMap.get(imageUrl);
      } else {
        List<ImageCache> imageCaches = mImageCacheDao.queryBuilder()
            .where(ImageCacheDao.Properties.Url.eq(imageUrl))
            .build()
            .list();
        if (!imageCaches.isEmpty()) {
          String path = imageCaches.get(0).getPath();
          if (!TextUtils.isEmpty(path) && FileUtils.exist(path)) {
            imageMap.put(imageUrl, path);
            return path;
          }
        }

        if (taskArray.indexOf(imageUrl) < 0) {
          taskArray.add(imageUrl);
          DownLoadTask task = new DownLoadTask(imageUrl, index);
          task.execute();
        }
        return "file:///android_asset/hupu_thread_default.png";
      }
    }
  }

  private class DownLoadTask extends AsyncTask<Void, Void, String> {
    private String imageUrl;
    private int index;

    private DownLoadTask(String imageUrl, int index) {
      this.imageUrl = imageUrl;
      this.index = index;
    }

    @Override protected String doInBackground(Void... params) {
      try {
        // 下载图片
        File imgFile =
            new File(ConfigUtils.getCachePath() + File.separator + SecurityUtils.getMD5(imageUrl)
                //+ imageUrl.substring(imageUrl.lastIndexOf(".")));
                + ".png");
        logger.debug("imgFile:" + imgFile.getName());
        mOkHttpHelper.httpDownload(imageUrl, imgFile);
        String path = imgFile.getAbsolutePath();
        if (!TextUtils.isEmpty(path)) {
          imageMap.put(imageUrl, path);
          mImageCacheDao.queryBuilder()
              .where(ImageCacheDao.Properties.Url.eq(imageUrl))
              .buildDelete()
              .executeDeleteWithoutDetachingEntities();
          ImageCache cache = new ImageCache(null, imageUrl, path);
          mImageCacheDao.insert(cache);
        }
        return path;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(String s) {
      super.onPostExecute(s);
      if (!TextUtils.isEmpty(s)) {
        mContentView.loadUrl("javascript:replaceImage(\"" + s + "\"," + index + ");");
      }
    }
  }
}
