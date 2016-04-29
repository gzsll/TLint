package com.gzsll.hupu.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.gzsll.hupu.helper.ConfigHelper;
import com.gzsll.hupu.helper.FormatHelper;
import com.gzsll.hupu.helper.OkHttpHelper;
import com.gzsll.hupu.helper.StringHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.view.PictureView;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/4/29.
 */
public class PicturePresenter extends Presenter<PictureView> {

    @Inject
    FormatHelper mFormatHelper;
    @Inject
    ConfigHelper mConfigHelper;
    @Inject
    OkHttpHelper mOkHttpHelper;
    @Inject
    ToastHelper mToastHelper;
    @Inject
    Activity mActivity;
    @Inject
    StringHelper mStringHelper;


    @Inject
    @Singleton
    public PicturePresenter() {

    }

    public void saveImage(final String url) {
        Observable.just(url).map(new Func1<String, InputStream>() {
            @Override
            public InputStream call(String s) {
                return getImageBytesFromLocal(Uri.parse(s));
            }
        }).map(new Func1<InputStream, File>() {
            @Override
            public File call(InputStream in) {
                if (in != null) {
                    String fileName = mFormatHelper.getFileNameFromUrl(url);
                    File target = new File(mConfigHelper.getPicSavePath(), fileName);
                    if (target.exists()) {
                        return target;
                    }
                    try {
                        BufferedSink sink = Okio.buffer(Okio.sink(target));
                        sink.writeAll(Okio.source(in));
                        sink.close();
                        return target;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).map(new Func1<File, File>() {
            @Override
            public File call(File file) {
                if (file != null && file.exists()) {
                    return file;
                }
                try {
                    mOkHttpHelper.httpDownload(url, file);
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).doOnNext(new Action1<File>() {
            @Override
            public void call(File file) {
                if (file != null && file.exists()) {
                    scanPhoto(file);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                if (file != null && file.exists()) {
                    mToastHelper.showToast("保存成功");
                } else {
                    mToastHelper.showToast("保存失败，请重试");
                }
            }
        });
    }

    private InputStream getImageBytesFromLocal(Uri loadUri) {
        if (loadUri == null) {
            return null;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        try {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey).openStream();
            }
            if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey).openStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mActivity.sendBroadcast(mediaScanIntent);
    }

    public void copyImagePath(String url) {
        mStringHelper.copy(url);
    }


    @Override
    public void detachView() {

    }
}
