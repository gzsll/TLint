package com.gzsll.hupu.ui.messagelist;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.component.ApplicationComponent;
import com.gzsll.hupu.injector.module.ActivityModule;
import com.gzsll.hupu.ui.pmlist.PmListFragment;

import dagger.Component;

/**
 * Created by sll on 2016/5/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface MessageComponent {
    void inject(MessageListFragment fragment);

    void inject(PmListFragment fragment);
}
