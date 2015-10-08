package com.gzsll.hupu.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sll on 2015/3/7.
 */
public class FormatHelper {

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

}
