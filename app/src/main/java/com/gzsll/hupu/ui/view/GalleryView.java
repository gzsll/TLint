package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Folder;
import com.gzsll.hupu.bean.Image;
import com.gzsll.hupu.ui.BaseView;

import java.util.List;

/**
 * Created by sll on 2016/3/9.
 */
public interface GalleryView extends BaseView {


    void renderFolders(List<Folder> folders);

    void renderImages(List<Image> images);

}
