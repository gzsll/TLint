package com.gzsll.hupu.ui.main;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.component.ApplicationComponent;
import com.gzsll.hupu.injector.module.ActivityModule;
import com.gzsll.hupu.ui.forum.ForumListFragment;
import com.gzsll.hupu.ui.thread.special.SpecialThreadListFragment;

import dagger.Component;

/**
 * Created by sll on 2016/5/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, MainModule.class
})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(SpecialThreadListFragment fragment);

    void inject(ForumListFragment fragment);
}
