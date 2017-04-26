package com.gzsll.hupu.ui.setting;

import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.injector.component.ApplicationComponent;
import com.gzsll.hupu.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by sll on 2016/5/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface SettingComponent {
    void inject(SettingFragment fragment);

    void inject(ColorsDialogFragment fragment);
}
