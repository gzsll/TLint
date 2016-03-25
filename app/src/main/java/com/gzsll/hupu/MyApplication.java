package com.gzsll.hupu;

import android.app.Application;
import android.os.Environment;

import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.FileHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.injector.component.ApplicationComponent;
import com.gzsll.hupu.injector.component.DaggerApplicationComponent;
import com.gzsll.hupu.injector.module.ApplicationModule;

import org.apache.log4j.Level;

import java.util.List;

import javax.inject.Inject;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by sll on 2016/3/8.
 */
public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;


    @Inject
    UserStorage mUserStorage;
    @Inject
    UserDao mUserDao;
    @Inject
    FileHelper mFileHelper;
    @Inject
    ConfigHelper mConfigHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        initLogger();
        initUser();
    }

    private void initComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void initLogger() {
        String path = getLogPath();
        try {
            final LogConfigurator lc = new LogConfigurator();
            lc.setFileName(path);
            lc.setFilePattern("%d - [%-6p-%c] - %m%n");
            lc.setMaxBackupSize(2);
            lc.setMaxFileSize(1024 * 1024);
            lc.setRootLevel(Level.DEBUG);
            // Set log level of a specific logger
            lc.setLevel("org.apache", Level.DEBUG);
            lc.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLogPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/gzsll/log.txt";
    }

    private void initUser() {
        List<User> users = mUserDao.queryBuilder().where(UserDao.Properties.Uid.eq(mSettingPrefHelper.getLoginUid())).list();
        if (!users.isEmpty()) {
            mUserStorage.login(users.get(0));
        }
    }




}
