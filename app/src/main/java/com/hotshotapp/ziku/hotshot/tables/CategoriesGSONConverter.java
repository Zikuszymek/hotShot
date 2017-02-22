package com.hotshotapp.ziku.hotshot.tables;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ziku on 2017-02-22.
 */

public class CategoriesGSONConverter {

    @SerializedName("idweb_page_category")
    private int idWebPage;

    @SerializedName("category_type")
    private String categoryType;

    public CategoriesGSONConverter() {
    }

    public int getIdWebPage() {
        return idWebPage;
    }

    public void setIdWebPage(int idWebPage) {
        this.idWebPage = idWebPage;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public void convertToActiveCategory(){
        ActiveCategories activeCategories = ActiveCategories.load(ActiveCategories.class,getIdWebPage());
        if(activeCategories == null){
            ActiveCategories newActiveCategory = new ActiveCategories(getCategoryType());
            newActiveCategory.save();
        } else{
            activeCategories.categoryType = getCategoryType();
            activeCategories.save();
        }
    }
}
