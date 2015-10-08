package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.storage.bean.Cover;
import com.gzsll.hupu.storage.bean.GroupThread;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.utils.FormatHelper;
import com.gzsll.hupu.utils.SettingPrefHelper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/3/7.
 */
@EViewGroup(R.layout.item_list_thread)
public class ThreadListItem extends LinearLayout {

    Logger logger = Logger.getLogger(ThreadListItem.class.getSimpleName());

    public SettingPrefHelper mSettingPrefHelper;
    public FormatHelper formatHelper;
    public BaseActivity mActivity;


    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvUserName, tvSingleTime;
    @ViewById
    TextView tvUser;
    @ViewById
    TextView tvTime;
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvSummary;
    @ViewById
    GridLayout grid;
    @ViewById
    TextView tvReply;
    @ViewById
    TextView tvLight;
    @ViewById
    LinearLayout llThreadItem, llUser;
    @ViewById
    RelativeLayout rlLight;

    private GroupThread thread;


    public ThreadListItem(Context context) {
        super(context);
    }

    public ThreadListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(GroupThread thread) {
        this.thread = thread;
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettingPrefHelper.getTitleSize());
        rlLight.setVisibility(GONE);
        if (thread.getLight() > 0) {
            tvLight.setText(String.valueOf(thread.getLight()));
            tvLight.setVisibility(View.VISIBLE);
        } else {
            tvLight.setVisibility(View.GONE);
        }
        tvReply.setText(String.valueOf(thread.getReply()));
        tvTitle.setText(thread.getTitle());
        if (mSettingPrefHelper.getSingleLine()) {
            llUser.setVisibility(GONE);
            tvSingleTime.setVisibility(VISIBLE);
            tvSummary.setVisibility(GONE);
            grid.setVisibility(View.GONE);
            tvSingleTime.setText(formatHelper.dateFormat(thread.getCreateAtUnixtime()));
        } else {
            llUser.setVisibility(VISIBLE);
            tvSingleTime.setVisibility(GONE);
            tvSummary.setVisibility(VISIBLE);
            grid.setVisibility(View.VISIBLE);
            ivIcon.setImageURI(Uri.parse(thread.getUserInfo().getIcon()));
            tvUserName.setText(thread.getUsername());
            tvTime.setText(formatHelper.dateFormat(thread.getCreateAtUnixtime()));
            tvSummary.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettingPrefHelper.getTextSize());
            tvSummary.setText(thread.getNote());
            buildMultiPic(thread, grid);
        }

    }

    protected void buildMultiPic(final GroupThread thread, final GridLayout gridLayout) {
        if (mSettingPrefHelper.getLoadPic()) {
            gridLayout.setVisibility(View.VISIBLE);
            final int count = thread.getCovers().size();
            final List<String> pics = new ArrayList<String>();
            for (int i = 0; i < count; i++) {
                SimpleDraweeView imageView = (SimpleDraweeView) gridLayout.getChildAt(i);
                imageView.setVisibility(View.VISIBLE);
                final Cover threadPic = thread.getCovers().get(i);
                pics.add(threadPic.getUrl());
                imageView.setImageURI(Uri.parse(mSettingPrefHelper.getLoadOriginPic() ? threadPic.getUrl() : threadPic.getUrlSmall()));
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePreviewActivity_.intent(mActivity).extraPic(threadPic.getUrl()).extraPics(pics).start();
                    }
                });
            }

            if (count < 9) {
                for (int i = 8; i > count - 1; i--) {
                    SimpleDraweeView pic = (SimpleDraweeView) gridLayout.getChildAt(i);
                    pic.setVisibility(View.GONE);
                }
            }
        } else {
            gridLayout.setVisibility(GONE);
        }
    }

    @Click
    void ivIcon() {
        UserProfileActivity_.intent(getContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).uid(String.valueOf(thread.getUserInfo().getUid())).start();
    }


}
