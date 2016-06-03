package com.gzsll.hupu.injector.module;

import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.data.local.ContentLocalDataSource;
import com.gzsll.hupu.data.local.ForumLocalDataSource;
import com.gzsll.hupu.data.remote.ContentRemoteDataSource;
import com.gzsll.hupu.data.remote.ForumRemoteDataSource;
import com.gzsll.hupu.db.ForumDao;
import com.gzsll.hupu.db.ThreadInfoDao;
import com.gzsll.hupu.db.ThreadReplyDao;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by sll on 2016/6/3.
 */
@Module public class RepositoryModule {

  @Singleton @Provides ContentLocalDataSource provideContentLocalDataSource(
      ThreadInfoDao mThreadInfoDao, ThreadReplyDao mThreadReplyDao) {
    return new ContentLocalDataSource(mThreadInfoDao, mThreadReplyDao);
  }

  @Singleton @Provides ContentRemoteDataSource provideContentRemoteDataSource(ForumApi mForumApi) {
    return new ContentRemoteDataSource(mForumApi);
  }

  @Singleton @Provides ForumLocalDataSource provideForumLocalDataSource(ForumDao mForumDao) {
    return new ForumLocalDataSource(mForumDao);
  }

  @Singleton @Provides ForumRemoteDataSource provideForumRemoteDataSource(ForumApi mForumApi) {
    return new ForumRemoteDataSource(mForumApi);
  }
}
