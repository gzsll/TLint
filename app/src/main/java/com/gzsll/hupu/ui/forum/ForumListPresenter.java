package com.gzsll.hupu.ui.forum;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.gzsll.hupu.data.ForumRepository;
import com.gzsll.hupu.db.Forum;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.otto.DelForumAttentionEvent;
import com.gzsll.hupu.service.OffLineService;
import com.gzsll.hupu.util.ToastUtil;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
@PerActivity
public class ForumListPresenter implements ForumListContract.Presenter {

    private final ForumRepository mForumRepository;
    private final Context mContext;
    private final Bus mBus;

    private ForumListContract.View mForumListView;
    private Subscription mSubscription;

    @Inject
    public ForumListPresenter(ForumRepository mForumRepository, Context mContext, Bus mBus) {
        this.mForumRepository = mForumRepository;
        this.mContext = mContext;
        this.mBus = mBus;
    }

    @Override
    public void onForumListReceive(final String forumId) {
        mForumListView.showLoading();
        mSubscription = mForumRepository.getForumList(forumId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Forum>>() {
                    @Override
                    public void call(List<Forum> fora) {
                        if (fora == null || fora.isEmpty()) {
                            mForumListView.onError();
                        } else {
                            mForumListView.hideLoading();
                            mForumListView.renderForumList(fora);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onForumAttentionDelClick(final Forum forum) {
        mForumRepository.removeForum(forum.getFid())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    ToastUtil.showToast("取消关注成功");
                    mBus.post(new DelForumAttentionEvent(forum.getFid()));
                    mForumListView.removeForum(forum);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtil.showToast("取消关注失败，请重试");
            }
        });
    }

    @Override
    public void onForumOfflineClick(Forum forum) {
        Intent intent = new Intent(mContext, OffLineService.class);
        ArrayList<Forum> forums = new ArrayList<>();
        forums.add(forum);
        intent.putExtra(OffLineService.EXTRA_FORUMS, forums);
        intent.setAction(OffLineService.START_DOWNLOAD);
        mContext.startService(intent);
    }

    @Override
    public void onForumClick(Forum forum) {
        mForumListView.showThreadUi(forum.getFid());
    }

    @Override
    public void attachView(@NonNull ForumListContract.View view) {
        mForumListView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mForumListView = null;
    }
}
