package com.example.ziku.hotshot.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.ziku.hotshot.WebPageFabric;

/**
 * Created by Ziku on 2016-07-26.
 */
public class WebSiteTable {

    public static final String TABLE_NAME = "web_sites";

    public static class WebSiteColumns implements BaseColumns{
        public static final String NAME = "web_site_name";
        public static final String URL = "adress_url";
        public static final String ACTIVE = "is_active";
        public static final String NOTIFICATION = "notify_me";
    }

    public static void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + WebSiteTable.TABLE_NAME + " (");
        sb.append(WebSiteColumns._ID + " INTEGER PRIMARY KEY, ");
        sb.append(WebSiteColumns.NAME + " TEXT NOT NULL, ");
        sb.append(WebSiteColumns.URL + " TEXT NOT NULL, " );
        sb.append(WebSiteColumns.ACTIVE + " INTEGER NOT NULL, ");
        sb.append(WebSiteColumns.NOTIFICATION + " INTEGER NOT NULL");
        sb.append(");");
        db.execSQL(sb.toString());
        Log.v("COS", sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + WebSiteTable.TABLE_NAME);
        WebSiteTable.onCreate(db);
    }

    public static void insertBaseData(SQLiteDatabase db){
        db.insertOrThrow(WebSiteTable.TABLE_NAME,null, newWebSiteTypes(WebPageFabric.X_KOM, WebPageFabric.X_KOM_URL, 1,1));
        db.insertOrThrow(WebSiteTable.TABLE_NAME,null, newWebSiteTypes(WebPageFabric.KOMPUTRONIK, WebPageFabric.KOMPUTRONIK_URL, 1,0));
        db.insertOrThrow(WebSiteTable.TABLE_NAME,null, newWebSiteTypes(WebPageFabric.MORELE, WebPageFabric.MORELE_URL, 1,0));
        db.insertOrThrow(WebSiteTable.TABLE_NAME,null, newWebSiteTypes(WebPageFabric.PROLINE, WebPageFabric.PROLINE_URL, 1,0));
        db.insertOrThrow(WebSiteTable.TABLE_NAME,null, newWebSiteTypes(WebPageFabric.SATYSFAKCJA, WebPageFabric.SATYSFAKCJA_URL, 1,1));
    }

    public static ContentValues newWebSiteTypes(String name, String url, int active, int notification){
        ContentValues cv = new ContentValues();
        cv.put(WebSiteColumns.NAME, name);
        cv.put(WebSiteColumns.URL, url);
        cv.put(WebSiteColumns.ACTIVE, active);
        cv.put(WebSiteColumns.NOTIFICATION, notification);
        return cv;
    }
}
