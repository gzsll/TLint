package com.gzsll.hupu.ui.thread.list;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2016/5/13.
 */
@Module
public class ThreadListModule {

    private String fid;

    public ThreadListModule(String fid) {
        this.fid = fid;
    }

    @Provides
    String provideFid() {
        return fid;
    }
}
