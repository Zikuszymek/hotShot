package com.example.ziku.hotshot.tables;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.ziku.hotshot.services.ActiveAsyncRefresh;

import java.util.Date;

/**
 * Created by Ziku on 2016-11-19.
 */

@Table(name = ActiveCategories.CATEGORIES_TABLE)
public class ActiveCategories extends Model{

    public static final String CATEGORIES_TABLE = "categories_table";
    public static final String CATEGORY_TYPE = "category_type";

    @Column(name = CATEGORY_TYPE)
    public String categoryType;

    public ActiveCategories(){}

    public ActiveCategories(String categoryType) {
        this.categoryType = categoryType;
    }

    public static Cursor getAllAsCursor(){
        String tableName = Cache.getTableInfo(ActiveWebSites.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").from(ActiveWebSites.class).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(resultRecords,null);
        return cursor;
    }
}
