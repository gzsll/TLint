package com.gzsll.hupu.ui.content;

import android.support.annotation.NonNull;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.ThreadLightReplyData;
import com.gzsll.hupu.bean.ThreadReplyData;
import com.gzsll.hupu.db.ThreadInfo;
import com.gzsll.hupu.db.ThreadReply;
import com.gzsll.hupu.util.HtmlUtils;
import com.gzsll.hupu.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/5/25.
 */
public class ContentPagerPresenter implements ContentPagerContract.Presenter {

  private ForumApi mForumApi;

  private ContentPagerContract.View mContentView;

  private Subscription mInfoSubscription;
  private Subscription mLightSubscription;
  private Subscription mReplySubscription;

  private List<ThreadReply> lightReplies = new ArrayList<>();
  private List<ThreadReply> replies = new ArrayList<>();
  private String fid;
  private String tid;

  @Inject public ContentPagerPresenter(ForumApi mForumApi) {
    this.mForumApi = mForumApi;
  }

  @Override
  public void onThreadInfoReceive(final String tid, final String fid, String pid, final int page) {
    this.fid = fid;
    this.tid = tid;
    if (page == 1) {
      mInfoSubscription = mForumApi.getThreadInfo(tid, fid, page, pid)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<ThreadInfo>() {
            @Override public void call(ThreadInfo threadInfo) {
              String content = threadInfo.getContent();
              threadInfo.setContent(HtmlUtils.transImgToLocal(content));
              mContentView.sendMessageToJS("addThreadInfo", threadInfo);
              loadLightReplies(tid, fid);
              loadReplies(tid, fid, page);
              mContentView.hideLoading();
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

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

    private int light;
    private String pid;

    public AddLight(int light, String pid) {
      this.light = light;
      this.pid = pid;
    }
  }

  private void loadLightReplies(String tid, String fid) {
    mLightSubscription = mForumApi.getThreadLightReplyList(tid, fid)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ThreadLightReplyData>() {
          @Override public void call(ThreadLightReplyData threadLightReplyData) {
            if (threadLightReplyData != null && threadLightReplyData.status == 200) {
              lightReplies = threadLightReplyData.list;
              if (threadLightReplyData.all_count > 0) {
                mContentView.sendMessageToJS("addLightTitle", "\"这些回帖亮了\"");
                for (int i = 0; i < threadLightReplyData.list.size(); i++) {
                  ThreadReply reply = threadLightReplyData.list.get(i);
                  reply.setIndex(i);
                  mContentView.sendMessageToJS("addLightPost", reply);
                }
              }
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        });
  }

  private void loadReplies(String tid, String fid, final int page) {
    mReplySubscription = mForumApi.getThreadReplyList(tid, fid, page)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ThreadReplyData>() {
          @Override public void call(ThreadReplyData threadReplyData) {
            if (threadReplyData != null && threadReplyData.status == 200) {
              replies = threadReplyData.result.list;
              if (page == 1) {
                mContentView.sendMessageToJS("addReplyTitle", "\"全部回帖\"");
              }
              for (int i = 0; i < threadReplyData.result.list.size(); i++) {
                ThreadReply reply = threadReplyData.result.list.get(i);
                reply.setIndex(i);
                mContentView.sendMessageToJS("addReply", reply);
              }
            }
            mContentView.hideLoading();
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        });
  }

  @Override public void attachView(@NonNull ContentPagerContract.View view) {
    mContentView = view;
    mContentView.showLoading();
  }

  @Override public void detachView() {
    mContentView = null;
    if (mInfoSubscription != null && !mInfoSubscription.isUnsubscribed()) {
      mInfoSubscription.unsubscribe();
    }
    if (mLightSubscription != null && !mLightSubscription.isUnsubscribed()) {
      mLightSubscription.unsubscribe();
    }
    if (mReplySubscription != null && !mReplySubscription.isUnsubscribed()) {
      mReplySubscription.unsubscribe();
    }
  }
}
