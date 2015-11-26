package com.gzsll.hupu.support.storage.bean;

import java.util.List;


public class GroupList {


    public List<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private List<CategoryList> categoryList;
    private String categoryName;


    public GroupList() {

    }


}