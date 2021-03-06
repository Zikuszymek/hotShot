package com.hotshotapp.ziku.hotshot.tables;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ziku on 2016-11-19.
 */

@Table(name = ActiveCategories.CATEGORIES_TABLE)
public class ActiveCategories extends Model implements Parcelable{

    public static final String CATEGORIES_TABLE = "categories_table";
    public static final String CATEGORY_TYPE = "category_type";

    @Column(name = CATEGORY_TYPE)
    public String categoryType;

    public ActiveCategories(){}

    private ActiveCategories(Parcel parcel){
        categoryType = parcel.readString();
    }

    public ActiveCategories(String categoryType) {
        this.categoryType = categoryType;
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
        parcel.writeString(categoryType);
    }

    public static final Parcelable.Creator<ActiveCategories> CREATOR = new Parcelable.Creator<ActiveCategories>(){

        @Override
        public ActiveCategories createFromParcel(Parcel parcel) {
            return new ActiveCategories(parcel);
        }

        @Override
        public ActiveCategories[] newArray(int i) {
            return new ActiveCategories[i];
        }
    };
}
