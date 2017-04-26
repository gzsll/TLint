package com.gzsll.hupu.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sll on 2016/6/2.
 */
public class HtmlUtil {
    private static String htmlString;

    public static String getHtmlString(Context mContext) {
        if (!TextUtils.isEmpty(htmlString)) {
            return htmlString;
        }
        String assetHtml = FileUtil.stringFromAssetsFile(mContext, "hupu_thread.html");
        String cachePath = ConfigUtil.getCachePath();
        if (!TextUtils.isEmpty(assetHtml)) {
            htmlString = assetHtml.replace("{hupu.js}",
                    String.format("file://%s", cachePath + File.separator + "hupu_thread.js"))
                    .replace("{jockey.js}",
                            String.format("file://%s", cachePath + File.separator + "jockey.js"))
                    .replace("{zepto.js}",
                            String.format("file://%s", cachePath + File.separator + "zepto.js"));
        }
        return htmlString;
    }

    public static String transImgToLocal(String content) {
        Pattern pattern = Pattern.compile("<img(.+?)data_url=\"(.+?)\"(.+?)src=\"(.+?)\"(.+?)>");
        Matcher localMatcher = pattern.matcher(content);
        while (localMatcher.find()) {
            String imageUrl = localMatcher.group(4);
            String localUrl = transToLocal(imageUrl);
            String localPath = "file://" + ConfigUtil.getCachePath() + File.separator + localUrl;
            if (FileUtil.exist(localPath)) {
                content = content.replace(imageUrl, localUrl);
            }
        }
        localMatcher.reset();
        return content;
    }

    public static String transToLocal(String url) {
        return transToLocal(url, null);
    }

    public static String transToLocal(String url, String dir) {
        url = url.substring(url.lastIndexOf("/") + 1);
        if (!TextUtils.isEmpty(dir)) url = dir + url;
        return url;
    }
}
