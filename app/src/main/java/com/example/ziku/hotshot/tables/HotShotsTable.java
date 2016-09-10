package com.example.ziku.hotshot.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.ziku.hotshot.WebPageFabric;

/**
 * Created by Ziku on 2016-07-26.
 */
public class HotShotsTable {
    public static final String TABLE_NAME = "hot_shots";

    public static class HotShotsColumn implements BaseColumns{
        public static final String WEB_SITE_ID = "web_site_id_name";
        public static final String PRODUCT_NAME = "product_name";
        public static final String OLD_PRICE = "old_price";
        public static final String NEW_PRICE = "new_price";
        public static final String ITEMS_LEFT = "items_left";
        public static final String TIME_PERIOD = "time_change_period";
        public static final String LAST_CHECK = "time_last_check";
        public static final String IMG_URL = "img_url";
        public static final String PRODUCT_URL = "PRODUCT_URL";
    }

    public static void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + HotShotsTable.TABLE_NAME + " (");
        sb.append(HotShotsColumn._ID + " INTEGER PRIMARY KEY, ");
        sb.append(HotShotsColumn.WEB_SITE_ID + " INTEGER NOT NULL, ");
        sb.append(HotShotsColumn.PRODUCT_NAME + " TEXT NOT NULL, ");
        sb.append(HotShotsColumn.OLD_PRICE + " TEXT NOT NULL,");
        sb.append(HotShotsColumn.NEW_PRICE + " TEXT NOT NULL, ");
        sb.append(HotShotsColumn.ITEMS_LEFT + " TEXT NOT NULL,");
        sb.append(HotShotsColumn.TIME_PERIOD + " TEXT NOT NULL, ");
        sb.append(HotShotsColumn.LAST_CHECK + " INTEGER NOT NULL, ");
        sb.append(HotShotsColumn.PRODUCT_URL + " TEXT NOT NULL, ");
        sb.append(HotShotsColumn.IMG_URL + " TEXT NOT NULL, ");

        sb.append("FOREIGN KEY(" + HotShotsColumn.WEB_SITE_ID + ") REFERENCES " + WebSiteTable.TABLE_NAME +
                "(" + BaseColumns._ID + ")");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + HotShotsTable.TABLE_NAME);
        HotShotsTable.onCreate(db);
    }

    public static void insertBaseData(SQLiteDatabase db){
        db.insertOrThrow(HotShotsTable.TABLE_NAME, null, defaultHotShotsData(1, WebPageFabric.EMPTY, WebPageFabric.EMPTY,
                WebPageFabric.EMPTY, WebPageFabric.EMPTY,WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY));
        db.insertOrThrow(HotShotsTable.TABLE_NAME, null, defaultHotShotsData(2,WebPageFabric.EMPTY, WebPageFabric.EMPTY,
                WebPageFabric.EMPTY, WebPageFabric.EMPTY,WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY));
        db.insertOrThrow(HotShotsTable.TABLE_NAME, null, defaultHotShotsData(3,WebPageFabric.EMPTY, WebPageFabric.EMPTY,
                WebPageFabric.EMPTY, WebPageFabric.EMPTY,WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY));
        db.insertOrThrow(HotShotsTable.TABLE_NAME, null, defaultHotShotsData(4,WebPageFabric.EMPTY, WebPageFabric.EMPTY,
                WebPageFabric.EMPTY, WebPageFabric.EMPTY,WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY));
        db.insertOrThrow(HotShotsTable.TABLE_NAME, null, defaultHotShotsData(5,WebPageFabric.EMPTY, WebPageFabric.EMPTY,
                WebPageFabric.EMPTY, WebPageFabric.EMPTY,WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY, WebPageFabric.EMPTY));
    }

    public static ContentValues defaultHotShotsData(int id, String productName, String oldPrice, String newPrice, String itemsLeft,
                                                    String timePeriod, String lastCheck, String imgUrl, String productUrl){
        ContentValues cv = new ContentValues();
        cv.put(HotShotsColumn.WEB_SITE_ID, id);
        cv.put(HotShotsColumn.PRODUCT_NAME,productName);
        cv.put(HotShotsColumn.OLD_PRICE, oldPrice);
        cv.put(HotShotsColumn.NEW_PRICE, newPrice);
        cv.put(HotShotsColumn.ITEMS_LEFT, itemsLeft);
        cv.put(HotShotsColumn.TIME_PERIOD, timePeriod);
        cv.put(HotShotsColumn.LAST_CHECK, lastCheck);
        cv.put(HotShotsColumn.IMG_URL,imgUrl);
        cv.put(HotShotsColumn.PRODUCT_URL,productUrl);
        return cv;
    }


}
