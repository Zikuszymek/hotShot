package com.example.ziku.hotshot.services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;

import com.example.ziku.hotshot.BuildConfig;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.jsonservices.JsonCategoriesAsync;
import com.example.ziku.hotshot.jsonservices.JsonWebPagesAsync;
import com.example.ziku.hotshot.management.HotShotsActiveAdapter;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveInfo;

import java.util.List;

/**
 * Created by Ziku on 2016-11-27.
 */

public class UniversalRefresh {

    public static final int lighterOrangeColor = Color.parseColor("#FFCC99");
    public static final int orangeColor = Color.parseColor("#FF6600");
    public static final String LAST_DB_UPDATE = "LastDBUpdate";
    public static final String APP_VERSION = "AppVersion";
    public static final long HOURS_24 = 24 * 60 * 60 * 1000;
    public static final String INTERNET_ERROR = "Problem połączenia z internetem, oferty mogą się nie wyświetlać lub być nieaktualne.";

    public static void RefreshCategoriesAndWebPages(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {

            if (networkInfo.isConnectedOrConnecting()) {
                Log.d("TEST", "Connected or connecting");

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                long lastCheck = sharedPreferences.getLong(LAST_DB_UPDATE,0);
                final long currentTime = System.currentTimeMillis();

                AsyncTask<Void,Void,Void> refreshCategories = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        JsonCategoriesAsync.UpdateAllCategories();
                        JsonWebPagesAsync.UpdateAllWebPages();
                        editor.putLong(LAST_DB_UPDATE,currentTime).apply();
                        return null;
                    }
                };

                if(lastCheck == 0){
                    refreshCategories.execute();
                } else if(currentTime - lastCheck > HOURS_24){
                    refreshCategories.execute();
                } else {Log.d("TEST","DB up to date");}
            } else {
                AlerDialogWithMessage(INTERNET_ERROR,context);
                Log.d("TEST","No internet connection");
            }
        } else {
            AlerDialogWithMessage(INTERNET_ERROR,context);
            Log.d("TEST","Network info is null");
        }
    }

    public static void AddNewGlobalInformationIfDoesNotExist(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String appVersion = sharedPreferences.getString(APP_VERSION,"");
        String currentAppVersion = BuildConfig.VERSION_NAME;


        if(!appVersion.equals(currentAppVersion)){
            ActiveInfo.AddAllinterestingElement();
            editor.putString(APP_VERSION,currentAppVersion).apply();
        }
    }



    public static void RefreshAllIfPossible(Activity activity){
        ListView listView = (ListView) activity.findViewById(R.id.hot_shot_swipe_list);
        ListView electroList = (ListView) activity.findViewById(R.id.electronics_swipe_list);
        ListView bookList = (ListView) activity.findViewById(R.id.books_swipe_list);
        ListView otherList = (ListView) activity.findViewById(R.id.others_swipe_list);

        UniversalRefresh.UniwersalRefreshById(activity,listView,0);
        UniversalRefresh.UniwersalRefreshById(activity,electroList,1);
        UniversalRefresh.UniwersalRefreshById(activity,bookList,2);
        UniversalRefresh.UniwersalRefreshById(activity,otherList,3);
    }

    public static void UniwersalRefreshById(Activity activity, ListView listView, int selectedList){
        if(listView != null) {
            List<ActiveHotShots> activeWebSitesList = ActiveHotShots.ReturnAllActiveHotShotsActive(selectedList);
            HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(activity.getBaseContext(), activeWebSitesList);
            listView.setAdapter(null);
            listView.setAdapter(hotShotsAdapter);
            Log.d("LIST","list is not null");
        }
    }

    public static void AlerDialogWithMessage(String message, Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.warning));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
