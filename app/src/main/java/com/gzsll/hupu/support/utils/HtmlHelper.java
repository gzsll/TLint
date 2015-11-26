package com.gzsll.hupu.support.utils;

import android.text.TextUtils;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sll on 2015/11/26.
 */
public class HtmlHelper {
    Logger logger = Logger.getLogger("HtmlHelper");

    private String htmlString;
    private ConfigHelper mConfigHelper;
    private FileHelper mFileHelper;
    private OkHttpHelper mOkHttpHelper;


    public HtmlHelper(ConfigHelper mConfigHelper, FileHelper mFileHelper, OkHttpHelper mOkHttpHelper) {
        this.mConfigHelper = mConfigHelper;
        this.mFileHelper = mFileHelper;
        this.mOkHttpHelper = mOkHttpHelper;
    }

    public String getHtmlString() {
        if (!TextUtils.isEmpty(htmlString)) {
            return htmlString;
        }
        String assetHtml = mFileHelper.stringFromAssetsFile("hupu_post.html");
        String cachePath = mConfigHelper.getCachePath();
        htmlString = assetHtml.replace("{hupu.js}", String.format("file://%s", cachePath + File.separator + "hupu.js")).replace("{jockey.js}", String.format("file://%s", cachePath + File.separator + "jockey.js")).replace("{zepto.js}", String.format("file://%s", cachePath + File.separator + "zepto.js"));
        return htmlString;
    }


    public String transImgToLocal(String content, boolean download) {
        Pattern patternImgSrc = Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)>");
        Matcher localMatcher = patternImgSrc.matcher(content);
        while (localMatcher.find()) {
            String imageUrl = localMatcher.group(2);
            String localUrl = transToLocal(imageUrl);
            String localPath = mConfigHelper.getCachePath() + File.separator + localUrl;
            if (!mFileHelper.exist(localPath) && download) {
                try {
                    mOkHttpHelper.httpDownload(imageUrl, new File(localPath));
                } catch (Exception e) {
                    logger.debug("图片下载失败:" + imageUrl);
                }
            } else {
                content = content.replace(imageUrl, localUrl);
            }
        }
        localMatcher.reset();
        return content;
    }

    public String transToLocal(String url) {
        return transToLocal(url, null);
    }

    public String transToLocal(String url, String dir) {
        url = url.substring(url.lastIndexOf("/") + 1);
        if (!TextUtils.isEmpty(dir))
            url = dir + url;
        return url;
    }
}
