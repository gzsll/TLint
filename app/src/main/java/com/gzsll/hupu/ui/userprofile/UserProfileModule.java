package com.gzsll.hupu.ui.userprofile;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2016/5/13.
 */
@Module
public class UserProfileModule {
    private String uid;

    public UserProfileModule(String uid) {
        this.uid = uid;
    }

    @Provides
    String provideUid() {
        return uid;
    }
}
