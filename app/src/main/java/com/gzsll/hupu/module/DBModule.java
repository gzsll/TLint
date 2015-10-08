package com.gzsll.hupu.module;

import android.content.Context;

import com.gzsll.hupu.db.BoardDao;
import com.gzsll.hupu.db.DaoMaster;
import com.gzsll.hupu.db.DaoSession;
import com.gzsll.hupu.db.UserDao;

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


}
