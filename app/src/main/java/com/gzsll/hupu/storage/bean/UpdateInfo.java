package com.gzsll.hupu.storage.bean;

/**
 * Created by sll on 2015/10/8.
 */
public class UpdateInfo {
    private int versionCode;
    private String updateInfo;
    private String updateUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
