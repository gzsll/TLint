package com.gzsll.hupu.ui.main;

import android.text.TextUtils;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.api.game.GameApi;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.bean.PmData;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.UpdateAgent;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/5/13.
 */
@Module
public class MainModule {

    private MainActivity mActivity;

    public MainModule(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @PerActivity
    Observable<Integer> provideNotificationObservable(GameApi mGameApi,
                                                      ForumApi mForumApi) {
        return Observable.zip(mGameApi.queryPmList(""), mForumApi.getMessageList("", 1),
                new Func2<PmData, MessageData, Integer>() {
                    @Override
                    public Integer call(PmData pmData, MessageData messageData) {
                        int size = 0;
                        if (pmData != null) {
                            if (pmData.is_login == 0) {
                                return null;
                            }
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
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Provides
    @PerActivity
    UpdateAgent provideUpdateAgent(OkHttpHelper mOkHttpHelper) {
        return new UpdateAgent(mOkHttpHelper, mActivity);
    }
}
