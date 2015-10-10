package com.gzsll.hupu.support.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GroupList {

    private static final String FIELD_CATEGORY_LIST = "categoryList";
    private static final String FIELD_CATEGORY_NAME = "categoryName";


    @SerializedName(FIELD_CATEGORY_LIST)
    private List<CategoryList> mCategoryLists;
    @SerializedName(FIELD_CATEGORY_NAME)
    private String mCategoryName;


    public GroupList() {

    }

    public void setCategoryLists(List<CategoryList> categoryLists) {
        mCategoryLists = categoryLists;
    }

    public List<CategoryList> getCategoryLists() {
        return mCategoryLists;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCategoryName() {
        return mCategoryName;
    }


}