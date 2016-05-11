package com.gzsll.hupu.ui.gallery;

import com.gzsll.hupu.bean.Folder;
import com.gzsll.hupu.bean.Image;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/5/11.
 */
public interface GalleryContract {
    interface View extends BaseView {
        void renderFolders(List<Folder> folders);

        void renderImages(List<Image> images);
    }

    interface Presenter extends BasePresenter<View> {
        void onImageAndFolderReceive();
    }
}
