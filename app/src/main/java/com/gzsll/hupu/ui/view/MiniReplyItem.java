package com.gzsll.hupu.ui.view;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.MiniReplyListItem;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.PostActivity_;
import com.gzsll.hupu.ui.activity.ReplyDetailActivity;
import com.gzsll.hupu.ui.activity.UserProfileActivity_;
import com.gzsll.hupu.widget.TouchableMovementMethod;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

/**
 * Created by sll on 2015/6/23.
 */
@EViewGroup(R.layout.item_list_mini_reply)
public class MiniReplyItem extends LinearLayout {
    public static final String STR_COLON = " : ";
    public static final String STR_FLOORER = "  \u697c\u4e3b ";
    public static final String STR_SPACE = "    ";

    public BaseActivity mActivity;
    public UserStorage mUserStorage;


    Logger logger = Logger.getLogger(MiniReplyItem.class.getSimpleName());

    public MiniReplyItem(Context context) {
        super(context);
    }

    public MiniReplyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @ViewById
    TextView tvContent;
    @ViewById
    View deliver;

    public void initReply(final MiniReplyListItem item, int floorer, int position) {
        deliver.setVisibility(position == 0 ? GONE : VISIBLE);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(item.getUserInfo().getUsername());
        if (item.getUserInfo().getUid() == floorer) {
            stringBuffer.append(STR_FLOORER);
        }
        stringBuffer.append(STR_COLON);
        stringBuffer.append(Html.fromHtml(item.getContent()).toString());
        stringBuffer.append(STR_SPACE);
        stringBuffer.append(item.getFormatTime());
        String content = stringBuffer.toString();
        SpannableString spannableString = new SpannableString(content);
        logger.debug(spannableString);
        spannableString.setSpan(new ClickableSpan() {
            public void onClick(View view) {
                logger.debug("onClick:" + view);
                if (mActivity instanceof ReplyDetailActivity) {
                    UserProfileActivity_.intent(mActivity).uid(String.valueOf(item.getUserInfo().getUid())).start();
                }
            }

            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(mActivity.getResources().getColor(R.color.blue));
                textPaint.setUnderlineText(false);
            }
        }, 0, item.getUserInfo().getUsername().toCharArray().length, 33);

        if (item.getUserInfo().getUid() == floorer) {
            spannableString.setSpan(new BackgroundColorSpan(mActivity.getResources().getColor(R.color.blue)), item.getUserInfo().getUsername().toCharArray().length + 1, item.getUserInfo().getUsername().toCharArray().length + 5, 33);
            spannableString.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.base_text_white)), item.getUserInfo().getUsername().toCharArray().length + 1, item.getUserInfo().getUsername().toCharArray().length + 5, 33);
            spannableString.setSpan(new RelativeSizeSpan(0.7f), item.getUserInfo().getUsername().toCharArray().length + 1, item.getUserInfo().getUsername().toCharArray().length + 5, 33);
        }
        spannableString.setSpan(new RelativeSizeSpan(0.7f), content.toCharArray().length - item.getFormatTime().toCharArray().length, content.toCharArray().length, 33);
        tvContent.setMovementMethod(TouchableMovementMethod.getInstance());
        tvContent.setText(spannableString);
        tvContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity instanceof ReplyDetailActivity) {
                    PostActivity_.intent(mActivity).type(Constants.TYPE_AT).mTo(item.getUserInfo().getUsername()).groupThreadId(item.getGroupThreadId() + "").groupReplyId(item.getId() + "").start();
                }
            }
        });

    }

}
