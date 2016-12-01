package com.example.ziku.hotshot.jsonservices;

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;
import com.example.ziku.hotshot.tables.ActiveCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ziku on 2016-11-19.
 */

public class JsonCategoriesAsync{

    private static final String TAG = JsonCategoriesAsync.class.getSimpleName();
    private static final String WEB_URL = "http://hotshot.ziku.ayz.pl/categories/?format=json";

    public static void UpdateAllCategories(){
        JsonHttp jsonHttp = new JsonHttp();
        String jsonResponse = jsonHttp.JsonServiceCall(WEB_URL);
        Log.d(TAG,jsonResponse);
        if(jsonResponse != null){
            try{
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String idwebPageCategory = object.getString("idweb_page_category");
                    String categoryType = object.getString("category_type");
                    Log.d(TAG,idwebPageCategory);
                    Log.d(TAG,categoryType);

                    ActiveCategories activeCategories = ActiveCategories.load(ActiveCategories.class,Integer.parseInt(idwebPageCategory));

                    if(activeCategories == null){
                        ActiveCategories newActiveCategory = new ActiveCategories(categoryType);
                        newActiveCategory.save();
                        Log.d(TAG, "new category");

                    } else {
                        activeCategories.categoryType = categoryType;
                        activeCategories.save();
                        Log.d(TAG,"upgrade existing category");
                    }

                }
            } catch (JSONException ex){
                Log.d(TAG,"JsonException " + ex.toString());
            }
        } else {
            Log.d(TAG,"Could not get json from");
        }
    }
}
