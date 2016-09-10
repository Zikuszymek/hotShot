package com.example.ziku.hotshot.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ziku.hotshot.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Ziku on 2016-09-10.
 */
public class HotShotsORMLiteHelper {//extends OrmLiteSqliteOpenHelper{
//
//    private static final String DATABASE_NAME = "hotShotsOrm.db";
//    private static final int DATABASE_VERSION = 1;
//
//    private Dao<WebSiteORM,Integer> webSiteDao;
//
//
//    public HotShotsORMLiteHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.values.ormlite_config);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
//        try{
//            TableUtils.createTable(connectionSource, WebSiteORM.class);
//        } catch (SQLException ex){
//            Log.d("SQL","error during table creation");
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
//
//    }
//
//    public Dao<WebSiteORM, Integer> getWebSiteDao() throws SQLException{
//        if(webSiteDao == null){
//            webSiteDao = getDao(WebSiteORM.class);
//        }
//        return webSiteDao;
//    }
}
