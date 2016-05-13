package com.gzsll.hupu.ui.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.otto.MessageReadEvent;
import com.gzsll.hupu.ui.account.AccountActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.messagelist.MessageActivity;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;
import com.gzsll.hupu.util.ToastUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/17.
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter {


    private UserStorage mUserStorage;
    private UserDao mUserDao;
    private Bus mBus;
    private ForumApi mForumApi;
    private GameApi mGameApi;
    private Activity mActivity;


    private MainContract.View mMainView;
    private int count = 0;

    @Inject
    public MainPresenter(UserStorage userStorage, UserDao userDao, Bus bus, ForumApi forumApi, GameApi gameApi, Activity activity) {
        mUserStorage = userStorage;
        mUserDao = userDao;
        mBus = bus;
        mForumApi = forumApi;
        mGameApi = gameApi;
        mActivity = activity;
    }


    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
        mBus.register(this);
        initUserInfo();
        initNotification();

    }


    private void initUserInfo() {
        mMainView.renderUserInfo(isLogin() ? mUserStorage.getUser() : null);
    }


    private void initNotification() {
        if (isLogin()) {
            Observable.zip(mGameApi.queryPmList(""), mForumApi.getMessageList("", 1), new Func2<PmData, MessageData, Integer>() {
                @Override
                public Integer call(PmData pmData, MessageData messageData) {
                    int size = 0;
                    if (pmData != null) {
                        for (Pm pm : pmData.result.data) {
                            if (!TextUtils.isEmpty(pm.unread) && pm.unread.equals("1")) {
                                size++;
                            }
                        }
                    }

                    if (messageData != null && messageData.status == 200) {
                        size += messageData.result.list.size();
                    }
                    return size;
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    count = integer;
                    mMainView.renderNotification(integer);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            });
        }
    }

    @Override
    public void clickNotification() {
        if (isLogin()) {
            MessageActivity.startActivity(mActivity);
        } else {
            toLogin();
        }
    }

    @Override
    public void toLogin() {
        LoginActivity.startActivity(mActivity);
        ToastUtils.showToast("请先登录");
    }

    @Override
    public void clickCover() {
        if (isLogin()) {
            UserProfileActivity.startActivity(mActivity, mUserStorage.getUid());
        } else {
            toLogin();
        }
    }

    @Override
    public void showAccountMenu() {
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                final List<User> userList = mUserDao.queryBuilder().list();
                for (User bean : userList) {
                    if (bean.getUid().equals(mUserStorage.getUid())) {
                        userList.remove(bean);
                        break;
                    }
                }
                subscriber.onNext(userList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                final String[] items = new String[users.size() + 1];
                for (int i = 0; i < users.size(); i++)
                    items[i] = users.get(i).getUserName();
                items[items.length - 1] = "账号管理";
                mMainView.renderAccountList(users, items);

            }
        });
    }

    @Override
    public void onAccountItemClick(int position, final List<User> users, final String[] items) {
        if (position == items.length - 1) {
            // 账号管理
            AccountActivity.startActivity(mActivity);
        } else {
            mUserStorage.login(users.get(position));
            initUserInfo();
        }
    }

    @Override
    public void exist() {
        if (isCanExit()) {
            AppManager.getAppManager().AppExit(mActivity);
        }
    }

    private long mExitTime = 0;

    private boolean isCanExit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showToast("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    @Override
    public boolean isLogin() {
        return mUserStorage.isLogin();
    }


    @Override
    public void detachView() {
        mBus.unregister(this);
        count = 0;
    }

    @Subscribe
    public void onChangeThemeEvent(ChangeThemeEvent event) {
        mMainView.reload();
    }


    @Subscribe
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        initUserInfo();
    }

    @Subscribe
    public void onAccountChangeEvent(AccountChangeEvent event) {
        initUserInfo();
    }

    @Subscribe
    public void onMessageReadEvent(MessageReadEvent event) {
        if (count >= 1) {
            count--;
        }
        mMainView.renderNotification(count);
    }
}
