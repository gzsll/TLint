package com.gzsll.hupu.support.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.storage.bean.ThreadSpanned;
import com.gzsll.hupu.ui.activity.ImagePreviewActivity_;
import com.gzsll.hupu.widget.JustifyTextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sll on 2015/8/25.
 * 格式化回复布局
 */
public class ReplyViewHelper {

    private static final String COMPILE = "(?:<blockquote>([\\s\\S]*)</blockquote>)|(?:<img src=\"(.*?)\".*?>)|(</blockquote>([\\s\\S]*)<img)";
    private SettingPrefHelper mSettingPrefHelper;
    private Context mContext;
    private Set<String> images = new HashSet<>();
    private int index = 0;
    private ArrayMap<String, Spanned> spannedArrayMap = new ArrayMap<>();


    public ReplyViewHelper(SettingPrefHelper mSettingPrefHelper, Context mContext) {
        this.mSettingPrefHelper = mSettingPrefHelper;
        this.mContext = mContext;
    }

    public void addToView(List<ThreadSpanned> list, LinearLayout linearLayout) {
        for (ThreadSpanned threadSpanned : list) {
            if (threadSpanned.type == 0) {
                addQuoteView(threadSpanned.quoteSpanneds, linearLayout);
            } else if (threadSpanned.type == 1) {
                addContentView(threadSpanned.realContent, linearLayout);
            } else if (threadSpanned.type == 2) {
                if (mSettingPrefHelper.getLoadPic()) {
                    images.add(threadSpanned.realContent);
                    index++;
                    addImageView(threadSpanned, linearLayout);
                }
            }
        }
    }

    public List<ThreadSpanned> compileContent(String content) {
        Matcher matcher = Pattern.compile(COMPILE).matcher(content);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        List<ThreadSpanned> arrayList = new ArrayList();
        while (matcher.find()) {
            String group = matcher.group();
            if (group.startsWith("<blockquote")) {
                String replace = content.substring(matcher.start(), matcher.end()).replace("<blockquote>", "").replace("</blockquote>", "");
                ThreadSpanned spannedModel = new ThreadSpanned(0, replace, matcher.start(), matcher.end());
                spannedModel.quoteSpanneds = compileContent(replace);
                arrayList.add(spannedModel);
                i3 = matcher.end();
                i2 = matcher.end();
            } else if (group.startsWith("<img")) {
                if (matcher.start() > i2 + 1) {
                    String imgContent = content.substring(i2, matcher.start() - 1);
                    ThreadSpanned spannedModel2 = new ThreadSpanned(1, imgContent, i2, matcher.start() - 1);
                    spannedArrayMap.put(imgContent, Html.fromHtml(imgContent));
                    arrayList.add(spannedModel2);
                    i2 = matcher.end();
                }
                String replace = content.substring(matcher.start(), matcher.end()).replace("<img src=\"", "").replace("\">", "");
                ThreadSpanned threadSpanned = new ThreadSpanned(2, replace, matcher.start(), matcher.end());
                int indexOf = replace.indexOf("_w");
                if (indexOf != -1) {
                    String image = replace.substring(indexOf, replace.length());
                    int wIndex = image.indexOf("w");
                    int hIndex = image.indexOf("h");
                    int eIndex = image.indexOf(".");
                    if (wIndex >= 0 && hIndex > wIndex) {
                        int width = Integer.valueOf(image.substring(wIndex + 1, hIndex));
                        threadSpanned.w = width;
                    }
                    if (hIndex >= 0 && eIndex > hIndex) {
                        int height = Integer.valueOf(image.substring(hIndex + 1, eIndex));
                        threadSpanned.h = height;
                    }

                }
                arrayList.add(threadSpanned);
                i4 = matcher.end();
            } else if (group.startsWith("</blockquote>")) {
                group = content.substring(matcher.start(), matcher.end()).replace("</blockquote>", "").replace("<img", "");
                spannedArrayMap.put(group, Html.fromHtml(group));
                arrayList.add(new ThreadSpanned(1, group, matcher.start(), matcher.end()));
            }
        }

        if (i3 == 0) {  //没有引用
            if (i4 == 0) {  //没有图片
                ThreadSpanned spanned = new ThreadSpanned(1, content, 0, 0);
                spannedArrayMap.put(content, Html.fromHtml(content));
                arrayList.add(spanned);
            } else if (content.length() - 1 > i4 + 1) {  //有图片
                String i4Content = content.substring(i4, content.length() - 1);
                spannedArrayMap.put(i4Content, Html.fromHtml(i4Content));
                arrayList.add(new ThreadSpanned(1, i4Content, i4, content.toCharArray().length - 1));

            }
        } else if (i4 == 0 && content.length() > i3) {  //有引用没有图片
            String str = content.substring(i3, content.length());
            spannedArrayMap.put(str, Html.fromHtml(str));
            arrayList.add(new ThreadSpanned(1, str, i3, content.toCharArray().length));

        } else if (content.length() - 1 > i4 + 1) {  //有图片有引用
            String str = content.substring(i4, content.length() - 1);
            spannedArrayMap.put(str, Html.fromHtml(str));
            arrayList.add(new ThreadSpanned(1, str, i4, content.toCharArray().length - 1));

        }
        matcher.reset();
        return arrayList;

    }


    private void addQuoteView(List<ThreadSpanned> list, LinearLayout layout) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int topMargin = DisplayHelper.dip2px(mContext, 10.0f);

        linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_background_color));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = topMargin;
        layout.addView(linearLayout, layoutParams);
        addToView(list, linearLayout);

    }

    private void addContentView(String str, LinearLayout linearLayout) {
        JustifyTextView textView = new JustifyTextView(mContext);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setMText(Html.fromHtml(str));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettingPrefHelper.getTextSize());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int topMargin = DisplayHelper.dip2px(mContext, 10.0f);
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = topMargin;
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);

    }


    private void addImageView(final ThreadSpanned spanned, LinearLayout linearLayout) {
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(spanned.w, spanned.h);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = DisplayHelper.dip2px(mContext, 10.0f);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageURI(Uri.parse(spanned.realContent));
        linearLayout.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).extraPic(spanned.realContent).extraPics(new ArrayList<String>(images)).start();
            }
        });
    }
}
