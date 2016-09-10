package com.example.ziku.hotshot.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ziku on 2016-09-10.
 */
@DatabaseTable(tableName = "web_sites")
public class WebSiteORM {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String webSiteName;

    @DatabaseField(canBeNull = false)
    private String adresURL;

    @DatabaseField(canBeNull = false)
    private boolean isActive;

    @DatabaseField(canBeNull = false)
    private boolean notifyUser;

    public WebSiteORM(){};

    public WebSiteORM(String webSiteName, String adresURL, boolean isActive, boolean notifyUser){
        this.webSiteName = webSiteName;
        this.adresURL = adresURL;
        this.isActive = isActive;
        this.notifyUser = notifyUser;
    }

    public int getId() {
        return id;
    }

    public String getWebSiteName() {
        return webSiteName;
    }

    public void setWebSiteName(String webSiteName) {
        this.webSiteName = webSiteName;
    }

    public String getAdresURL() {
        return adresURL;
    }

    public void setAdresURL(String adresURL) {
        this.adresURL = adresURL;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotifyUser() {
        return notifyUser;
    }

    public void setNotifyUser(boolean notifyUser) {
        this.notifyUser = notifyUser;
    }
}