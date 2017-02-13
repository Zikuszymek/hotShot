package com.hotshotapp.ziku.hotshot.jsonservices;

import android.content.Context;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.tables.ActiveCategories;
import com.hotshotapp.ziku.hotshot.tables.ActiveWebSites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ziku on 2016-11-19.
 */

public class JsonWebPagesAsync{

    private static final String TAG = JsonWebPagesAsync.class.getSimpleName();
    private static final String WEB_URL = KeyItems.WEBPAGES;
    private Context context;

    public JsonWebPagesAsync(Context context) {
        this.context = context;
    }

    public void UpdateAllWebPages(){
        JsonHttp jsonHttp = new JsonHttp();
        String jsonResponse = jsonHttp.JsonServiceCall(WEB_URL,context);
        Log.d(TAG,jsonResponse);
        if(jsonResponse != null){
            try{
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String idWebPage = object.getString("id_web_page");
                    String webPageName = object.getString("name_web_page");
                    String webPageUrl = object.getString("url_web_page");
                    String isActive = object.getString("is_active_page");
                    String webPageCategory = object.getString("web_page_category");

                    Log.d(TAG,idWebPage);
                    Log.d(TAG,webPageName);
                    Log.d(TAG,webPageUrl);
                    Log.d(TAG,isActive);
                    Log.d(TAG,webPageCategory);

                    boolean isActiveFromServer;
                    if (isActive.equals("1")){
                        isActiveFromServer = true;
                    } else { isActiveFromServer = false;}

                    ActiveCategories activeCategory = ActiveCategories.load(ActiveCategories.class,Integer.parseInt(webPageCategory));
                    if(activeCategory != null) {

                        ActiveWebSites activeWebSite = ActiveWebSites.load(ActiveWebSites.class, Integer.parseInt(idWebPage));

                        if (activeWebSite == null) {
                            ActiveWebSites newActiveWebSite = new ActiveWebSites(webPageName, webPageUrl, true, true, isActiveFromServer, activeCategory);
                            newActiveWebSite.save();
                        } else {
                            activeWebSite.webSiteName = webPageName;
                            activeWebSite.webSIteUrl = webPageUrl;
                            activeWebSite.activeFromServer = isActiveFromServer;
                            activeWebSite.activeCategory = activeCategory;
                            activeWebSite.save();
                        }
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
