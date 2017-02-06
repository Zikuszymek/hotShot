package com.hotshotapp.ziku.hotshot.jsonservices;

import android.content.Context;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.tables.ActiveCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ziku on 2016-11-19.
 */

public class JsonCategoriesAsync{

    private static final String TAG = JsonCategoriesAsync.class.getSimpleName();
    private static final String WEB_URL = KeyItems.categories;
    private Context context;

    public JsonCategoriesAsync(Context context) {
        this.context = context;
    }

    public void UpdateAllCategories(){
        JsonHttp jsonHttp = new JsonHttp();
        String jsonResponse = jsonHttp.JsonServiceCall(WEB_URL,context);
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
