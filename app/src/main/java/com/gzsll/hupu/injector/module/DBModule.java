package com.gzsll.hupu.injector.module;

import android.content.Context;
import com.gzsll.hupu.db.DaoMaster;
import com.gzsll.hupu.db.DaoSession;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.UserDao;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by sll on 2015/3/4.
 */
@Module public class DBModule {

  @Provides @Singleton DaoMaster.DevOpenHelper provideDevOpenHelper(Context context) {
    return new DaoMaster.DevOpenHelper(context, "app.db", null);
  }

  @Provides @Singleton DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper helper) {
    return new DaoMaster(helper.getWritableDatabase());
  }

  @Provides @Singleton DaoSession provideDaoSession(DaoMaster master) {
    return master.newSession();
  }

  @Provides @Singleton ForumDao getForumDao(DaoSession session) {
    return session.getForumDao();
  }

  @Provides @Singleton UserDao getUserDao(DaoSession session) {
    return session.getUserDao();
  }
}
