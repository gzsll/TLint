package com.gzsll.hupu.module;

import com.gzsll.hupu.ui.activity.ContentActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gzsll on 2014/8/23 0023.
 */
@Module(
        complete = false, library = true
)
public class ContentModule {
    private ContentActivity mActivity;

    public ContentModule(ContentActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    ContentActivity provideContentActivity() {
        return mActivity;
    }

}
