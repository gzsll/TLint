package com.gzsll.hupu.injector.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gzsll.hupu.injector.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sll on 2016/1/6.
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }


}
