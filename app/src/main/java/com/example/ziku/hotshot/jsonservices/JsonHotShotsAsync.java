package com.example.ziku.hotshot.jsonservices;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by Ziku on 2016-11-19.
 */

public class JsonHotShotsAsync extends AsyncTask<Void,Void,Void>{

    private static final String TAG = JsonHotShotsAsync.class.getSimpleName();
    private static final String WEB_URL = "http://hotshot.ziku.ayz.pl/hotshots/?format=json";

    @Override
    protected Void doInBackground(Void... voids) {
        JsonHttp jsonHttp = new JsonHttp();
        String jsonResponse = jsonHttp.JsonServiceCall(WEB_URL);
        Log.d(TAG,jsonResponse);
        if(jsonResponse != null){
            try{
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String idHotShot = object.getString("id_hot_shot");
                    String productName = object.getString("product_name");
                    String oldPrice = object.getString("old_price");
                    String newPrice = object.getString("new_price");
                    String lastCheck = object.getString("last_check");
                    String productUrl = object.getString("product_url");
                    String imbUrl = object.getString("img_url");
                    String webPage = object.getString("web_page");

                    Log.d(TAG,idHotShot);
                    Log.d(TAG,productName);
                    Log.d(TAG,oldPrice);
                    Log.d(TAG,newPrice);
                    Log.d(TAG,lastCheck);
                    Log.d(TAG,productUrl);
                    Log.d(TAG,imbUrl);
                    Log.d(TAG,webPage);

                    Date now = new Date();

                    ActiveWebSites activeWebSite = ActiveWebSites.load(ActiveWebSites.class, Integer.parseInt(webPage));
                    if(activeWebSite != null){
                        ActiveHotShots activeHotShot = ActiveHotShots.load(ActiveHotShots.class, Integer.parseInt(idHotShot));
                        if(activeHotShot == null){
                            ActiveHotShots newActiveHotShot = new ActiveHotShots(productName,Integer.parseInt(oldPrice),Integer.parseInt(newPrice),
                                    imbUrl,productUrl,activeWebSite,JsonUpdateDB.EMPTY,now);
                            newActiveHotShot.save();

                            Log.d(TAG, "new hotshot");
                        } else {
                            activeHotShot.productName = productName;
                            activeHotShot.oldPrice = Integer.parseInt(oldPrice);
                            activeHotShot.newPrice = Integer.parseInt(newPrice);
                            activeHotShot.lastNewChange = now;
                            activeHotShot.lastNotyfication = "";
                            activeHotShot.imgUrl = imbUrl;
                            activeHotShot.productUrl = productUrl;
                            activeHotShot.webSites = activeWebSite;
                            activeHotShot.save();

                            Log.d(TAG, "upgrade hotshot");
                        }
                    }
                }
            } catch (JSONException ex){
                Log.d(TAG,"JsonException " + ex.toString());
            }
        } else {
            Log.d(TAG,"Could not get json from");
        }
        return null;
    }
}
