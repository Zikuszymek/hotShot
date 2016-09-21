package com.example.ziku.hotshot.tables;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.Date;

/**
 * Created by Ziku on 2016-09-11.
 */
@Table(name = ActiveWebSites.WEB_SITE_TABLE)
public class ActiveWebSites extends Model{

    public static final String WEB_SITE_TABLE = "web_sites_table";
    public static final String WEB_SITE_NAME = "web_site_name";
    public static final String WEB_SITE_URL = "web_site_url";
    public static final String IS_ACTIVE = "is_active";
    public static final String NOTIFY_USER = "notify_user";
    public static final String TIME_PERIOD = "time_period";
    public static final String LAST_CHECK = "last_check";

    @Column(name = WEB_SITE_NAME)
    public String webSiteName;

    @Column(name = WEB_SITE_URL)
    public String webSIteUrl;

    @Column(name = IS_ACTIVE)
    public boolean isActive;

    @Column(name = NOTIFY_USER)
    public boolean notifyUser;

    @Column(name = TIME_PERIOD)
    public long timePeriod;

    @Column(name = LAST_CHECK)
    public Date lastCheck;

    public ActiveWebSites(){
        super();
    }

    public ActiveWebSites(String webSiteName, String webSIteUrl, boolean isActive, boolean notifyUser, long timePeriod, Date lastCheck) {
        this.lastCheck = lastCheck;
        this.webSiteName = webSiteName;
        this.webSIteUrl = webSIteUrl;
        this.isActive = isActive;
        this.notifyUser = notifyUser;
        this.timePeriod = timePeriod;
    }

    public static Cursor getAllAsCursor(){
        String tableName = Cache.getTableInfo(ActiveWebSites.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").from(ActiveWebSites.class).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(resultRecords,null);
        return cursor;
    }
}