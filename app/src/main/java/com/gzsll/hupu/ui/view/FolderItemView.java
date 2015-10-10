package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.bean.Folder;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

/**
 * Created by sll on 2015/9/17.
 */
@EViewGroup(R.layout.item_list_folder)
public class FolderItemView extends RelativeLayout {
    @ViewById
    SimpleDraweeView cover;
    @ViewById
    TextView name;
    @ViewById
    TextView size;
    @ViewById
    ImageView indicator;

    public FolderItemView(Context context) {
        super(context);
    }

    public FolderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(List<Folder> mFolders, int position, int lastSelected) {
        if (position == 0) {
            name.setText("所有图片");
            size.setText(getTotalImageSize(mFolders) + "张");
            if (mFolders.size() > 0) {
                Folder f = mFolders.get(0);
                cover.setImageURI(Uri.fromFile(new File(f.cover.path)));
            }
        } else {
            Folder folder = mFolders.get(position);
            name.setText(folder.name);
            size.setText(folder.images.size() + "张");
            // 显示图片
            cover.setImageURI(Uri.fromFile(new File(folder.cover.path)));
        }
        if (lastSelected == position) {
            indicator.setVisibility(View.VISIBLE);
        } else {
            indicator.setVisibility(View.INVISIBLE);
        }
    }

    private int getTotalImageSize(List<Folder> mFolders) {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }
}
