package com.gzsll.hupu.ui.content;

import android.support.annotation.NonNull;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.data.ContentRepository;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.otto.UpdateContentPageEvent;
import com.gzsll.hupu.util.ToastUtils;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.List;
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

  private ContentPagerContract.View mContentView;

  private Subscription mInfoSubscription;
  private Subscription mLightSubscription;
  private Subscription mReplySubscription;

  private List<ThreadReply> lightReplies = new ArrayList<>();
  private List<ThreadReply> replies = new ArrayList<>();
  private String fid;
  private String tid;

  @Inject
  public ContentPagerPresenter(ContentRepository mContentRepository, ForumApi mForumApi, Bus mBus) {
    this.mContentRepository = mContentRepository;
    this.mForumApi = mForumApi;
    this.mBus = mBus;
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
              //loadLightReplies(tid, fid);
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
}
