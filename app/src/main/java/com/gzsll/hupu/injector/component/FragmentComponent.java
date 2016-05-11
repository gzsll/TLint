package com.gzsll.hupu.injector.component;

import android.app.Activity;

import com.gzsll.hupu.injector.PerFragment;
import com.gzsll.hupu.injector.module.FragmentModule;
import com.gzsll.hupu.ui.BaseFragment;
import com.gzsll.hupu.ui.browser.BrowserFragment;
import com.gzsll.hupu.ui.content.ContentFragment;
import com.gzsll.hupu.ui.forum.ForumListFragment;
import com.gzsll.hupu.ui.imagepreview.ImageFragment;
import com.gzsll.hupu.ui.messagelist.MessageListFragment;
import com.gzsll.hupu.ui.pmdetail.PmDetailFragment;
import com.gzsll.hupu.ui.pmlist.PmListFragment;
import com.gzsll.hupu.ui.setting.SettingFragment;
import com.gzsll.hupu.ui.thread.list.ThreadListFragment;
import com.gzsll.hupu.ui.thread.special.SpecialThreadListFragment;

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



    void inject(ContentFragment mContentFragment);


    void inject(BrowserFragment mBrowserFragment);

    void inject(ForumListFragment mForumListFragment);

    void inject(SettingFragment mSettingFragment);


    void inject(MessageListFragment mMessageListFragment);

    void inject(ImageFragment imageFragment);

    void inject(PmListFragment mPmListFragment);


    void inject(PmDetailFragment mPmDetailFragment);

    void inject(SpecialThreadListFragment mSpecialThreadListFragment);

}
