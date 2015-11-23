package com.gzsll.hupu;

import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.gzsll.hupu.module.RootModule;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.squareup.okhttp.OkHttpClient;

import org.androidannotations.annotations.EApplication;
import org.apache.log4j.Level;
import org.litepal.LitePalApplication;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * @author gzsll
 */
@EApplication
public class AppApplication extends LitePalApplication {


    @Inject
    OkHttpClient mOkHttpClient;
    @Inject
    UserStorage mUserStorage;
    @Inject
    UserDao mUserDao;

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies();
        initLogger();
        initUser();
        initFrescoConfig();
        LitePalApplication.initialize(this);
    }

    private void injectDependencies() {
        objectGraph = ObjectGraph.create(new RootModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }


    private void initLogger() {
        String path = getLogPath();
        try {
            final LogConfigurator lc = new LogConfigurator();
            lc.setFileName(getFilesDir().getAbsolutePath() + "/gzsll/log.txt");
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
        List<User> users = mUserDao.queryBuilder().where(UserDao.Properties.IsLogin.eq(true)).list();
        if (!users.isEmpty()) {
            mUserStorage.setUser(users.get(0));
        }
    }

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;

    private void initFrescoConfig() {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, mOkHttpClient).setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder()
                                .setBaseDirectoryPath(getCacheDir())
                                .setBaseDirectoryName("imageCache")
                                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                                .build()).build();
        Fresco.initialize(this, config);

    }


}
