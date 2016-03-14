package com.gzsll.hupu.injector.component;

import android.app.Activity;

import com.gzsll.hupu.injector.PerFragment;
import com.gzsll.hupu.injector.module.FragmentModule;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.fragment.BrowserFragment;
import com.gzsll.hupu.ui.fragment.ContentFragment;
import com.gzsll.hupu.ui.fragment.ForumListFragment;
import com.gzsll.hupu.ui.fragment.MDColorsDialogFragment;
import com.gzsll.hupu.ui.fragment.MessageListFragment;
import com.gzsll.hupu.ui.fragment.SettingFragment;
import com.gzsll.hupu.ui.fragment.ThreadCollectFragment;
import com.gzsll.hupu.ui.fragment.ThreadListFragment;
import com.gzsll.hupu.ui.fragment.ThreadRecommendFragment;

import dagger.Component;

/**
 * Created by sll on 2016/1/6.
 */
@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(BaseFragment mBaseFragment);

    void inject(ThreadListFragment mThreadListFragment);

    void inject(ThreadRecommendFragment mThreadRecommendFragment);

    void inject(ContentFragment mContentFragment);

    void inject(ThreadCollectFragment mThreadCollectFragment);

    void inject(BrowserFragment mBrowserFragment);

    void inject(ForumListFragment mForumListFragment);

    void inject(SettingFragment mSettingFragment);

    void inject(MDColorsDialogFragment mMDColorsDialogFragment);

    void inject(MessageListFragment mMessageListFragment);


}
