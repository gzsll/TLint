package com.gzsll.hupu.ui.pmdetail;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.PmDetail;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.content.ContentActivity;
import com.gzsll.hupu.ui.thread.list.ThreadListActivity;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;
import com.gzsll.hupu.widget.MyMovementMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/5/6.
 */
public class PmDetailAdapter extends RecyclerView.Adapter<PmDetailAdapter.ViewHolder> {

    private UserStorage mUserStorage;
    private Activity mActivity;

    @Inject
    public PmDetailAdapter(UserStorage mUserStorage, Activity mActivity) {
        this.mUserStorage = mUserStorage;
        this.mActivity = mActivity;
    }

    private static final int TYPE_USER = 1;
    private static final int TYPE_OTHER = 2;

    private List<PmDetail> mPmDetails = new ArrayList<>();

    public void bind(List<PmDetail> pmDetails) {
        mPmDetails = pmDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        PmDetail detail = mPmDetails.get(position);
        if (detail.puid.equals(mUserStorage.getUid())) {
            return TYPE_USER;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public PmDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_pm_user, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_pm_other, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PmDetail detail = mPmDetails.get(position);
        holder.detail = detail;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(Long.valueOf(detail.create_time) * 1000);
        holder.tvDate.setText(format.format(date));
        if (!TextUtils.isEmpty(detail.header)) {
            holder.ivUser.setImageURI(Uri.parse(detail.header));
        }
        holder.tvContent.setMovementMethod(MyMovementMethod.getInstance());
        holder.tvContent.setText(a(detail.content));
    }

    @Override
    public int getItemCount() {
        return mPmDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        PmDetail detail;

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivUser)
        SimpleDraweeView ivUser;
        @BindView(R.id.pbReply)
        ProgressBar pbReply;
        @BindView(R.id.tvContent)
        TextView tvContent;

        @OnClick(R.id.ivUser)
        void ivUserClick() {
            UserProfileActivity.startActivity(mActivity, detail.puid);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private SpannableStringBuilder a(String content) {
        Matcher matcher = Pattern.compile("<a.+?</a>", Pattern.CASE_INSENSITIVE).matcher(content);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int i = 0;
        while (matcher.find()) {
            i = matcher.end();
            String group = matcher.group();
            spannableStringBuilder.append(content.substring(0, i - group.length()));
            spannableStringBuilder.append(span(content.substring(i - group.length(), i)));
        }
        if (i <= 0) {
            spannableStringBuilder.append(content);
        } else {
            spannableStringBuilder.append(content.substring(i));
        }
        return spannableStringBuilder;
    }

    private CharSequence span(String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(Html.fromHtml(text));
        URLSpan[] spans = ssb.getSpans(0, ssb.length(), URLSpan.class);
        for (final URLSpan span : spans) {
            int start = ssb.getSpanStart(span);
            int end = ssb.getSpanEnd(span);
            ssb.removeSpan(span);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    handlerSpan(span);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return ssb;
    }

    private void handlerSpan(URLSpan span) {
        String url = span.getURL();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        switch (uri.getScheme()) {
            case "kanqiu":
                if (url.contains("topic")) {
                    String tid = uri.getLastPathSegment();
                    ContentActivity.startActivity(mActivity, "", tid, "", 1);
                } else if (url.contains("board")) {
                    String boardId = uri.getLastPathSegment();
                    ThreadListActivity.startActivity(mActivity, boardId);
                }
                break;
            case "app":
                break;
            case "http":
            case "https":
                BrowserActivity.startActivity(mActivity, url);
                break;
        }
    }
}
