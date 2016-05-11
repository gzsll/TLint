package com.gzsll.hupu.ui.imagepreview;

import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/5/11.
 */
public interface ImagePreviewContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {
        void saveImage(String url);

        void copyImagePath(String url);
    }
}
