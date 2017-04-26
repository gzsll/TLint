package com.gzsll.hupu.ui.report;

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
        ActivityModule.class, ReportModule.class
})
public interface ReportComponent extends ActivityComponent {

    void inject(ReportActivity activity);
}
