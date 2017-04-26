package com.gzsll.hupu.ui.gallery;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.gzsll.hupu.bean.Folder;
import com.gzsll.hupu.bean.Image;
import com.gzsll.hupu.injector.PerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/9.
 */
@PerActivity
public class GalleryPresenter implements GalleryContract.Presenter {

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID
    };

    private GalleryContract.View mGalleryView;
    private Subscription mSubscription;
    private List<Folder> folders = new ArrayList<>();
    private boolean hasFolderGened = false;

    private Context mContext;

    @Inject
    public GalleryPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull GalleryContract.View view) {
        mGalleryView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mGalleryView = null;
    }

    @Override
    public void onImageAndFolderReceive() {
        mSubscription = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null,
                        IMAGE_PROJECTION[2] + " DESC");
                subscriber.onNext(cursor);
            }
        }).subscribeOn(Schedulers.io()).map(new Func1<Cursor, List<Image>>() {
            @Override
            public List<Image> call(Cursor cursor) {
                List<Image> images = new ArrayList<Image>();
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!folders.contains(folder)) {
                                List<Image> imageList = new ArrayList<Image>();
                                imageList.add(image);
                                folder.images = imageList;
                                folders.add(folder);
                            } else {
                                // 更新
                                Folder f = folders.get(folders.indexOf(folder));
                                f.images.add(image);
                            }
                            hasFolderGened = true;
                        }
                    } while (cursor.moveToNext());
                }
                return images;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Image>>() {
            @Override
            public void call(List<Image> images) {
                mGalleryView.renderImages(images);
                mGalleryView.renderFolders(folders);
            }
        });
    }
}
