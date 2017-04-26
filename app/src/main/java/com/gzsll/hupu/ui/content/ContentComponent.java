package com.gzsll.hupu.ui.content;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.component.ActivityComponent;
import com.gzsll.hupu.injector.component.ApplicationComponent;
import com.gzsll.hupu.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by sll on 2016/5/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, ContentModule.class
})
public interface ContentComponent extends ActivityComponent {

    void inject(ContentActivity activity);

    void inject(ContentFragment mFragment);
}
