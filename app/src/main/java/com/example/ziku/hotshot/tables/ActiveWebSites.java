package com.example.ziku.hotshot.tables;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.Model;
/**
 * Created by Ziku on 2016-09-11.
 */
@Table(name = "web_sites")
public class ActiveWebSites extends Model{

    @Column(name = "web_site_name")
    public String webSiteName;

    @Column(name = "web_site_url")
    public String webSIteUrl;

    @Column(name = "is_active")
    public boolean isActive;

    @Column(name = "notify_user")
    public boolean notifyUser;

    public ActiveWebSites(){
        super();
    }

    public ActiveWebSites(String webSiteName, String webSIteUrl, boolean isActive, boolean notifyUser){
        super();
        this.webSiteName = webSiteName;
        this.webSIteUrl = webSIteUrl;
        this.isActive = isActive;
        this.notifyUser = notifyUser;
    }

}