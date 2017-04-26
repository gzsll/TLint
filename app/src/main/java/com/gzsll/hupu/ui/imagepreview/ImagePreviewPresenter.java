package com.gzsll.hupu.ui.imagepreview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.gzsll.hupu.components.okhttp.OkHttpHelper;
import com.gzsll.hupu.injector.PerActivity;
import com.gzsll.hupu.util.ConfigUtil;
import com.gzsll.hupu.util.FormatUtil;
import com.gzsll.hupu.util.StringUtil;
import com.gzsll.hupu.util.ToastUtil;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;

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
@PerActivity
public class ImagePreviewPresenter implements ImagePreviewContract.Presenter {

    private OkHttpHelper mOkHttpHelper;
    private Context mContext;

    @Inject
    public ImagePreviewPresenter(OkHttpHelper okHttpHelper, Context context) {
        mOkHttpHelper = okHttpHelper;
        mContext = context;
    }

    @Override
    public void saveImage(final String url) {
        Observable.just(url)
                .map(new Func1<String, InputStream>() {
                    @Override
                    public InputStream call(String s) {
                        return getImageBytesFromLocal(Uri.parse(s));
                    }
                })
                .map(new Func1<InputStream, File>() {
                    @Override
                    public File call(InputStream in) {
                        if (in != null) {
                            String fileName = FormatUtil.getFileNameFromUrl(url);
                            File target = new File(ConfigUtil.getPicSavePath(mContext), fileName);
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
                })
                .map(new Func1<File, File>() {
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
                })
                .doOnNext(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file != null && file.exists()) {
                            scanPhoto(file);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file != null && file.exists()) {
                            ToastUtil.showToast("保存成功");
                        } else {
                            ToastUtil.showToast("保存失败，请重试");
                        }
                    }
                });
    }

    private InputStream getImageBytesFromLocal(Uri loadUri) {
        if (loadUri == null) {
            return null;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
        try {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getMainFileCache()
                        .getResource(cacheKey)
                        .openStream();
            }
            if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getSmallImageFileCache()
                        .getResource(cacheKey)
                        .openStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void copyImagePath(String url) {
        StringUtil.copy(mContext, url);
    }

    @Override
    public void attachView(@NonNull ImagePreviewContract.View view) {

    }

    @Override
    public void detachView() {

    }
}
