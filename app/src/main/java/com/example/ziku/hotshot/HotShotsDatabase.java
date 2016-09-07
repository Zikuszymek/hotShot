package com.example.ziku.hotshot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.CursorAdapter;

import com.example.ziku.hotshot.tables.HotShotsTable;
import com.example.ziku.hotshot.tables.WebSiteTable;

/**
 * Created by Ziku on 2016-07-26.
 */
public class HotShotsDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "hotShots.db";
    private static final int DB_VERSION = 1;
    private static HotShotsDatabase hotShotsDatabase = null;
    private Context context;
    private WebPageFabric webPageFabric;

    public synchronized static HotShotsDatabase ReturnSingleInstance(Context context){
        if(hotShotsDatabase == null)
            hotShotsDatabase = new HotShotsDatabase(context);
        return hotShotsDatabase;
    }

    private HotShotsDatabase(final Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
        this.webPageFabric = new WebPageFabric();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        WebSiteTable.onCreate(sqLiteDatabase);
        HotShotsTable.onCreate(sqLiteDatabase);

        WebSiteTable.insertBaseData(sqLiteDatabase);
        HotShotsTable.insertBaseData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        WebSiteTable.onUpgrade(sqLiteDatabase,newVersion,oldVersion);
        HotShotsTable.onUpgrade(sqLiteDatabase,newVersion, oldVersion);
    }

    public Cursor GetAllSettings()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + WebSiteTable.WebSiteColumns._ID + " , " + WebSiteTable.WebSiteColumns.NAME + " , " + WebSiteTable.WebSiteColumns.ACTIVE +
        " , " + WebSiteTable.WebSiteColumns.NOTIFICATION + " FROM " + WebSiteTable.TABLE_NAME,null);
        return cursor;
    }

    public void SetIfWebPageIsActive(int id, boolean active){
        ContentValues cv = new ContentValues();
        cv.put(WebSiteTable.WebSiteColumns.ACTIVE,active);
        SQLiteDatabase db = getReadableDatabase();
        db.update(WebSiteTable.TABLE_NAME,cv,"_id="+id,null);
        Log.d("DB","active changed");
    }

    public void SetIfWebPAgeNotify(int id, boolean notify){
        ContentValues cv = new ContentValues();
        cv.put(WebSiteTable.WebSiteColumns.NOTIFICATION,notify);
        SQLiteDatabase db = getReadableDatabase();
        db.update(WebSiteTable.TABLE_NAME,cv,"_id="+id,null);
        Log.d("DB","notify changed");
    }

    public void UpdateHotShot(String webPageName)
    {
        ContentValues cv = this.webPageFabric.returnNewWebSiteData(webPageName);
        int id = GetIDProvidedWebPage(webPageName);
        SQLiteDatabase db = getReadableDatabase();
        db.update(HotShotsTable.TABLE_NAME,cv,"_id="+id,null);
    }

    public int GetIDProvidedWebPage(String webPageName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + WebSiteTable.WebSiteColumns._ID + " FROM " +
                WebSiteTable.TABLE_NAME + " WHERE " + WebSiteTable.WebSiteColumns.NAME + "=?", new String[] {webPageName});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public String GetProductName(String web){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + HotShotsTable.HotShotsColumn.PRODUCT_NAME + " FROM " + HotShotsTable.TABLE_NAME +
                " WHERE " + HotShotsTable.HotShotsColumn.WEB_SITE_ID + "=?", new String[] {String.valueOf(GetIDProvidedWebPage(web))} );
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public Cursor GetImageAndName(String webPageName){
        String query = "SELECT " + HotShotsTable.HotShotsColumn.IMG_URL + " , " + HotShotsTable.HotShotsColumn.PRODUCT_NAME + " , " + WebSiteTable.WebSiteColumns.NAME + " FROM " +
              HotShotsTable.TABLE_NAME + " INNER JOIN " + WebSiteTable.TABLE_NAME + " ON " + HotShotsTable.TABLE_NAME + "." + HotShotsTable.HotShotsColumn.WEB_SITE_ID +
                " = " + WebSiteTable.TABLE_NAME + "." + WebSiteTable.WebSiteColumns._ID + " WHERE " + WebSiteTable.WebSiteColumns.NAME + " = \"" + webPageName + "\"";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
    public Cursor GetAllHotShots(){
//        String[] kolumny = {HotShotsTable.HotShotsColumn._ID,HotShotsTable.HotShotsColumn.PRODUCT_NAME, HotShotsTable.HotShotsColumn.OLD_PRICE,
//                HotShotsTable.HotShotsColumn.NEW_PRICE, HotShotsTable.HotShotsColumn.ITEMS_LEFT};
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(HotShotsTable.TABLE_NAME, kolumny,null,null,null,null,null);

        String query = "SELECT " + HotShotsTable.TABLE_NAME + "." + HotShotsTable.HotShotsColumn._ID + " , " + HotShotsTable.HotShotsColumn.PRODUCT_NAME +
                " , " + HotShotsTable.HotShotsColumn.OLD_PRICE + " , " + HotShotsTable.HotShotsColumn.NEW_PRICE + " , " +
                HotShotsTable.HotShotsColumn.ITEMS_LEFT + " , " + HotShotsTable.HotShotsColumn.PRODUCT_URL + " , " +
                WebSiteTable.WebSiteColumns.NAME + " FROM " + HotShotsTable.TABLE_NAME + " INNER JOIN " + WebSiteTable.TABLE_NAME +
                " ON " + HotShotsTable.TABLE_NAME + "." + HotShotsTable.HotShotsColumn.WEB_SITE_ID +
                " = " + WebSiteTable.TABLE_NAME + "." + WebSiteTable.WebSiteColumns._ID + " WHERE " + HotShotsTable.HotShotsColumn.PRODUCT_NAME + "!=\"-\"";
        Log.d("DB-TEST",query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor GetAllActiveHotShots(){
        String query = "SELECT " + HotShotsTable.TABLE_NAME + "." + HotShotsTable.HotShotsColumn._ID + " , " + HotShotsTable.HotShotsColumn.PRODUCT_NAME +
                " , " + HotShotsTable.HotShotsColumn.OLD_PRICE + " , " + HotShotsTable.HotShotsColumn.NEW_PRICE + " , " +
                HotShotsTable.HotShotsColumn.ITEMS_LEFT + " , " + HotShotsTable.HotShotsColumn.PRODUCT_URL + " , " +
                WebSiteTable.WebSiteColumns.NAME + " ," + WebSiteTable.WebSiteColumns.ACTIVE + " FROM " + HotShotsTable.TABLE_NAME + " INNER JOIN " + WebSiteTable.TABLE_NAME +
                " ON " + HotShotsTable.TABLE_NAME + "." + HotShotsTable.HotShotsColumn.WEB_SITE_ID +
                " = " + WebSiteTable.TABLE_NAME + "." + WebSiteTable.WebSiteColumns._ID + " WHERE " +  WebSiteTable.WebSiteColumns.ACTIVE + " != 0 AND " +  HotShotsTable.HotShotsColumn.PRODUCT_NAME + " !=\"-\"";
//                WebSiteTable.WebSiteColumns.ACTIVE + " >0";
//        Log.d("DB-TEST",query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
}
