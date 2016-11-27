package com.example.ziku.hotshot.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.jsonservices.JsonCategoriesAsync;
import com.example.ziku.hotshot.jsonservices.JsonHotShotsAsync;
import com.example.ziku.hotshot.jsonservices.JsonWebPagesAsync;
import com.example.ziku.hotshot.management.HotShotsActiveAdapter;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

/**
 * Created by Ziku on 2016-11-27.
 */

public class UniversalRefresh {

    public static final int lighterOrangeColor = Color.parseColor("#FFCC99");
    public static final int orangeColor = Color.parseColor("#FF6600");

    public static void GetAllJsonDataRefreshed(Activity activity, SwipeRefreshLayout swipeRefreshLayout){

        Void returnStatement;
        JsonCategoriesAsync jsonCategoriesAsync = new JsonCategoriesAsync();
        try {
            returnStatement = jsonCategoriesAsync.execute().get();
        } catch (Exception ex) {}

        JsonWebPagesAsync webPagesAsync = new JsonWebPagesAsync();
        try {
            returnStatement = webPagesAsync.execute().get();
        } catch (Exception ex) {}

        JsonHotShotsAsync hotShotsAsync = new JsonHotShotsAsync();
        try {
            returnStatement = hotShotsAsync.execute().get();
        } catch (Exception ex) {}

        RefreshElectronicList(activity);
        swipeRefreshLayout.setRefreshing(false);
    }

    public static void RefreshElectronicList(Activity activity){
        ListView listView = (ListView) activity.findViewById(R.id.electronics_swipe_list);
        List<ActiveHotShots> activeWebSitesList =  ActiveHotShots.ReturnAllActiveHotShotsActive(1);
        HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(activity.getBaseContext(), activeWebSitesList);
        listView.setAdapter(null);
        listView.setAdapter(hotShotsAdapter);
    }
}
