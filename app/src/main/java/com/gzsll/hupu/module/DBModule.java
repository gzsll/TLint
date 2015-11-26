package com.gzsll.hupu.module;

import android.content.Context;

import com.gzsll.hupu.support.db.BoardDao;
import com.gzsll.hupu.support.db.DBGroupThreadDao;
import com.gzsll.hupu.support.db.DBGroupsDao;
import com.gzsll.hupu.support.db.DBMiniReplyListItemDao;
import com.gzsll.hupu.support.db.DBThreadInfoDao;
import com.gzsll.hupu.support.db.DBThreadReplyItemDao;
import com.gzsll.hupu.support.db.DBUserInfoDao;
import com.gzsll.hupu.support.db.DaoMaster;
import com.gzsll.hupu.support.db.DaoSession;
import com.gzsll.hupu.support.db.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2015/3/4.
 */
@Module(
        complete = false,
        library = true
)
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
    BoardDao getContentDao(DaoSession session) {
        return session.getBoardDao();
    }

    @Provides
    @Singleton
    UserDao getUserDao(DaoSession session) {
        return session.getUserDao();
    }

    @Provides
    @Singleton
    DBThreadInfoDao provideDBThreadInfoDao(DaoSession session) {
        return session.getDBThreadInfoDao();
    }


    @Provides
    @Singleton
    DBGroupThreadDao provideDBGroupThreadDao(DaoSession session) {
        return session.getDBGroupThreadDao();
    }


    @Provides
    @Singleton
    DBMiniReplyListItemDao provideDBMiniReplyListItemDao(DaoSession session) {
        return session.getDBMiniReplyListItemDao();
    }


    @Provides
    @Singleton
    DBThreadReplyItemDao provideDBThreadReplyItemDao(DaoSession session) {
        return session.getDBThreadReplyItemDao();
    }

    @Provides
    @Singleton
    DBUserInfoDao provideDBUserInfoDao(DaoSession session) {
        return session.getDBUserInfoDao();
    }

    @Provides
    @Singleton
    DBGroupsDao provideDbGroupsDao(DaoSession session) {
        return session.getDBGroupsDao();
    }


}
