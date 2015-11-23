package com.gzsll.hupu.support.storage.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Badge extends DataSupport implements Serializable {


    private List<String> small = new ArrayList<>();


    public List<String> getSmall() {
        return small;
    }

    public void setSmall(List<String> small) {
        this.small = small;
    }
}