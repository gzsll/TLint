package com.gzsll.hupu.support.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Badge implements Serializable {

    private static final String FIELD_SMALL = "small";


    @SerializedName(FIELD_SMALL)
    private List<String> mSmalls;


    public Badge() {

    }

    public void setSmalls(List<String> smalls) {
        mSmalls = smalls;
    }

    public List<String> getSmalls() {
        return mSmalls;
    }


}