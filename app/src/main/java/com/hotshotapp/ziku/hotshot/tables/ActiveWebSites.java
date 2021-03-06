package com.hotshotapp.ziku.hotshot.tables;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Cache;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ziku on 2016-09-11.
 */
@Table(name = ActiveWebSites.WEB_SITE_TABLE)
public class ActiveWebSites extends Model implements Parcelable{

    public static final String WEB_SITE_TABLE = "web_sites_table";
    public static final String WEB_SITE_NAME = "name_web_page";
    public static final String WEB_SITE_URL = "url_web_page";
    public static final String IS_ACTIVE = "is_active";
    public static final String NOTIFY_USER = "notify_user";
    public static final String ACTIVE_FROM_SERVER = "is_active_page";
    public static final String WEB_CATEGORY = "web_page_category";

    @Column(name = WEB_SITE_NAME)
    public String webSiteName;

    @Column(name = WEB_SITE_URL)
    public String webSIteUrl;

    @Column(name = IS_ACTIVE)
    public boolean isActive;

    @Column(name = NOTIFY_USER)
    public boolean notifyUser;

    @Column(name = ACTIVE_FROM_SERVER)
    public boolean activeFromServer;

    @Column(name = WEB_CATEGORY)
    public ActiveCategories activeCategory;

    public ActiveWebSites(){
        super();
    }

    public ActiveWebSites(String webSiteName, String webSIteUrl, boolean isActive, boolean notifyUser, boolean activeFromServer, ActiveCategories activeCategory) {
        this.activeCategory = activeCategory;
        this.webSiteName = webSiteName;
        this.webSIteUrl = webSIteUrl;
        this.isActive = isActive;
        this.notifyUser = notifyUser;
        this.activeFromServer = activeFromServer;
    }

    public static Cursor getAllAsCursor(){
        String tableName = Cache.getTableInfo(ActiveWebSites.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").from(ActiveWebSites.class).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(resultRecords,null);
        return cursor;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(activeCategory,i);
        parcel.writeString(webSiteName);
        parcel.writeString(webSIteUrl);
        parcel.writeInt(isActive ? 1 : 0);
        parcel.writeInt(notifyUser ? 1 : 0);
        parcel.writeInt(activeFromServer ? 1 :0);
    }

    private ActiveWebSites(Parcel parcel){
        this.activeCategory = parcel.readParcelable(ActiveCategories.class.getClassLoader());
        this.webSiteName = parcel.readString();
        this.webSIteUrl = parcel.readString();
        this.isActive = parcel.readInt()!=0;
        this.notifyUser = parcel.readInt()!=0;
        this.activeFromServer = parcel.readInt()!=0;
    }

    public static final Parcelable.Creator<ActiveWebSites> CREATOR = new Creator<ActiveWebSites>() {
        @Override
        public ActiveWebSites createFromParcel(Parcel parcel) {
            return new ActiveWebSites(parcel);
        }

        @Override
        public ActiveWebSites[] newArray(int i) {
            return new ActiveWebSites[i];
        }
    };
}