package com.hotshotapp.ziku.hotshot.tables;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ziku on 2017-02-22.
 */

public class WebPagesGSONConverter {

    private int id_web_page;
    private String name_web_page;
    private String url_web_page;
    private int is_active_page;
    private int web_page_category;

    public WebPagesGSONConverter() {
    }

    public int getId_web_page() {
        return id_web_page;
    }

    public void setId_web_page(int id_web_page) {
        this.id_web_page = id_web_page;
    }

    public String getName_web_page() {
        return name_web_page;
    }

    public void setName_web_page(String name_web_page) {
        this.name_web_page = name_web_page;
    }

    public String getUrl_web_page() {
        return url_web_page;
    }

    public void setUrl_web_page(String url_web_page) {
        this.url_web_page = url_web_page;
    }

    public int getIs_active_page() {
        return is_active_page;
    }

    public void setIs_active_page(int is_active_page) {
        this.is_active_page = is_active_page;
    }

    public int getWeb_page_category() {
        return web_page_category;
    }

    public void setWeb_page_category(int web_page_category) {
        this.web_page_category = web_page_category;
    }

    public void convertToActiveWebPage(){
        boolean isActiveFromServer;
        if (getIs_active_page()==1){
            isActiveFromServer = true;
        } else { isActiveFromServer = false;}

        ActiveCategories activeCategory = ActiveCategories.load(ActiveCategories.class,getWeb_page_category());
        if(activeCategory != null) {

            ActiveWebSites activeWebSite = ActiveWebSites.load(ActiveWebSites.class, getId_web_page());

            if (activeWebSite == null) {
                ActiveWebSites newActiveWebSite = new ActiveWebSites(getName_web_page(), getUrl_web_page(), true, true, isActiveFromServer, activeCategory);
                newActiveWebSite.save();
            } else {
                activeWebSite.webSiteName = getName_web_page();
                activeWebSite.webSIteUrl = getUrl_web_page();
                activeWebSite.activeFromServer = isActiveFromServer;
                activeWebSite.activeCategory = activeCategory;
                activeWebSite.save();
            }
        }
    }
}
