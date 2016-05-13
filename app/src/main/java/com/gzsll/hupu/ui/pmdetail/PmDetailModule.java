package com.gzsll.hupu.ui.pmdetail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2016/5/13.
 */
@Module
public class PmDetailModule {
    private String uid;

    public PmDetailModule(String uid) {
        this.uid = uid;
    }

    @Provides
    String provideUid() {
        return uid;
    }
}
