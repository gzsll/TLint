package com.gzsll.hupu.support.utils;

import android.text.TextUtils;

import com.gzsll.hupu.support.storage.bean.OfflinePictureInfo;

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

    public OfflinePictureInfo downloadImgToLocal(String content) {
        OfflinePictureInfo info = new OfflinePictureInfo();
        int offlineCount = 0;
        long offlineLength = 0;
        Pattern patternImgSrc = Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)>");
        Matcher localMatcher = patternImgSrc.matcher(content);
        while (localMatcher.find()) {
            String imageUrl = localMatcher.group(2);
            String localUrl = transToLocal(imageUrl);
            String localPath = mConfigHelper.getCachePath() + File.separator + localUrl;
            if (!mFileHelper.exist(localPath)) {
                try {
                    File local = new File(localPath);
                    mOkHttpHelper.httpDownload(imageUrl, local);
                    offlineCount++;
                    offlineLength += local.length();
                } catch (Exception e) {
                    logger.debug("图片下载失败:" + imageUrl);
                }
            }
        }
        localMatcher.reset();
        info.setOfflineCount(offlineCount);
        info.setOfflineLength(offlineLength);
        return info;
    }


    /**
     * 替换网页图片地址为本地地址
     *
     * @param content 网页内容
     * @return 替换后的内容
     */
    public String transImgToLocal(String content) {
        Pattern patternImgSrc = Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)>");
        Matcher localMatcher = patternImgSrc.matcher(content);
        while (localMatcher.find()) {
            String imageUrl = localMatcher.group(2);
            String localUrl = transToLocal(imageUrl);
            String localPath = mConfigHelper.getCachePath() + File.separator + localUrl;
            if (mFileHelper.exist(localPath)) {
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
