package com.gzsll.hupu.storage.bean;

import com.google.gson.annotations.SerializedName;


public class Cover {

    private static final String FIELD_URL_SMALL = "urlSmall";
    private static final String FIELD_HEIGHT = "height";
    private static final String FIELD_URL = "url";
    private static final String FIELD_WIDTH = "width";
    private static final String FIELD_BYTE = "byte";


    @SerializedName(FIELD_URL_SMALL)
    private String mUrlSmall;
    @SerializedName(FIELD_HEIGHT)
    private int mHeight;
    @SerializedName(FIELD_URL)
    private String mUrl;
    @SerializedName(FIELD_WIDTH)
    private int mWidth;
    @SerializedName(FIELD_BYTE)
    private String mByte;


    public Cover() {

    }

    public void setUrlSmall(String urlSmall) {
        mUrlSmall = urlSmall;
    }

    public String getUrlSmall() {
        return mUrlSmall;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setByte(String mByte) {
        this.mByte = mByte;
    }

    public String getByte() {
        return mByte;
    }


}