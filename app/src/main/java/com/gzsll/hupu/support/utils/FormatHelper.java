package com.gzsll.hupu.support.utils;

import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sll on 2015/3/7.
 */
public class FormatHelper {

    /**
     * 过滤html标签
     *
     * @param str html
     * @param tag 标签
     * @return 过滤后的html内容
     */
    public String fiterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String dateFormat(long timeIntivalSince1970) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return sdf.format(new Date((0x3e8 * timeIntivalSince1970)));
    }

    public long dateFormat(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            long timeStart = sdf.parse(date).getTime();
            return timeStart;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String purge(String content) {
        content = content.replace("'", "\\'");
        content = content.replace("\r", "");
        content = content.replace("\n", "");
        return content;
    }

    public String getFileNameFromUrl(String url) {
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        return filename;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 根据主页网址获取用户uid
     *
     * @param html 用户主页地址  etc:http://my.hupu.com/18786719
     * @return uid
     */
    public String getUid(String html) {
        if (!TextUtils.isEmpty(html)) {
            return html.substring(html.lastIndexOf("/") + 1);
        }
        return "0";
    }
}
