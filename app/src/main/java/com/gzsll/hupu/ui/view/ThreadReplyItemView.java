package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.BaseResult;
import com.gzsll.hupu.support.storage.bean.MiniReplyListItem;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.utils.ReplyViewHelper;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by sll on 2015/6/18.
 */
@EViewGroup(R.layout.item_list_reply)
public class ThreadReplyItemView extends LinearLayout {
    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvUserName;
    @ViewById
    TextView tvUser;
    @ViewById
    TextView tvTime;
    @ViewById
    LinearLayout llContent;
    @ViewById
    TextView tvReplyLight;
    @ViewById
    LinearLayout llMiniReply;
    @ViewById
    Button btMore;
    @ViewById
    View deliver;
    @ViewById
    ImageView ivLight;
    @ViewById
    RelativeLayout rlLight;

    public ThreadApi mThreadApi;
    public ReplyViewHelper mReplyViewHelper;
    public UserStorage mUserStorage;
    public BaseActivity mActivity;


    private ThreadReplyItem item;


    public ThreadReplyItemView(Context context) {
        super(context);
    }

    public ThreadReplyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(ThreadReplyItem item) {
        this.item = item;
        ivIcon.setImageURI(Uri.parse(item.getUserInfo().getIcon()));
        tvUserName.setText(item.getUserInfo().getUsername());
        tvTime.setText(item.getCreate_at());
        tvUser.setVisibility(VISIBLE);
        tvUser.setText(item.getFloor() + "楼");
        tvReplyLight.setVisibility(VISIBLE);
        tvReplyLight.setText(String.valueOf(item.getLights()));
        llContent.removeAllViews();
        mReplyViewHelper.addToView(mReplyViewHelper.compileContent(item.getContent()), llContent);
        if (item.getMiniReplyList() != null && !item.getMiniReplyList().getLists().isEmpty()) {
            llMiniReply.setVisibility(VISIBLE);
            deliver.setVisibility(VISIBLE);
            if (item.getMiniReplyList().getLists().size() >= 20) {
                btMore.setVisibility(VISIBLE);
            } else {
                btMore.setVisibility(GONE);
            }
            initMiniReply(llMiniReply, item.getMiniReplyList().getLists());
        } else {
            llMiniReply.setVisibility(GONE);
            btMore.setVisibility(GONE);
            deliver.setVisibility(GONE);
        }
    }

    @Click
    void rlLight() {
        mThreadApi.lightByApp(item.getGroupThreadId(), item.getId(), new retrofit.Callback<BaseResult>() {
            @Override
            public void success(BaseResult result, retrofit.client.Response response) {
                if (result != null && result.getStatus() == 200) {
                    ivLight.setImageResource(R.drawable.ic_list_light);
                    tvReplyLight.setText(String.valueOf(item.getLights() + 1));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void initMiniReply(LinearLayout llMiniReply, List<MiniReplyListItem> mLists) {
        llMiniReply.removeAllViews();
        for (int i = 0; i < mLists.size(); i++) {
            MiniReplyListItem item = mLists.get(i);
            MiniReplyItem view = MiniReplyItem_.build(mActivity);
            view.mUserStorage = mUserStorage;
            view.mActivity = mActivity;
            view.initReply(item, this.item.getUserInfo().getUid(), i);
            llMiniReply.addView(view);
        }

    }


    @Click
    void ivIcon() {
        UserProfileActivity_.intent(mActivity).uid(String.valueOf(item.getUserInfo().getUid())).start();
    }
}
