package com.gzsll.hupu.injector.module;

import android.content.Context;

import com.gzsll.hupu.db.DaoMaster;
import com.gzsll.hupu.db.DaoSession;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.ImageCacheDao;
import com.gzsll.hupu.db.ReadThreadDao;
import com.gzsll.hupu.db.ThreadDao;
import com.gzsll.hupu.db.ThreadInfoDao;
import com.gzsll.hupu.db.ThreadReplyDao;
import com.gzsll.hupu.db.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2015/3/4.
 */
@Module
public class DBModule {

    @Provides
    @Singleton
    DaoMaster.DevOpenHelper provideDevOpenHelper(Context context) {
        return new DaoMaster.DevOpenHelper(context, "app.db", null);
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper helper) {
        return new DaoMaster(helper.getWritableDatabase());
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }

    @Provides
    @Singleton
    ForumDao getForumDao(DaoSession session) {
        return session.getForumDao();
    }

    @Provides
    @Singleton
    UserDao getUserDao(DaoSession session) {
        return session.getUserDao();
    }

    @Provides
    @Singleton
    ThreadDao getThreadDao(DaoSession session) {
        return session.getThreadDao();
    }

    @Provides
    @Singleton
    ThreadInfoDao getThreadInfoDao(DaoSession session) {
        return session.getThreadInfoDao();
    }

    @Provides
    @Singleton
    ThreadReplyDao getThreadReplyDao(DaoSession session) {
        return session.getThreadReplyDao();
    }

    @Provides
    @Singleton
    ReadThreadDao getReadThreadDao(DaoSession session) {
        return session.getReadThreadDao();
    }

    @Provides
    @Singleton
    ImageCacheDao getImageCache(DaoSession session) {
        return session.getImageCacheDao();
    }
}
