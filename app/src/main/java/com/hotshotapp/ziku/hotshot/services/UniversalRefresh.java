package com.hotshotapp.ziku.hotshot.services;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.hotshotapp.ziku.hotshot.BuildConfig;
import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.AsyncTaskHotShot;
import com.hotshotapp.ziku.hotshot.jsonservices.JsonCategoriesAsync;
import com.hotshotapp.ziku.hotshot.jsonservices.JsonWebPagesAsync;
import com.hotshotapp.ziku.hotshot.management.HotShotsActiveAdapter;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;
import com.hotshotapp.ziku.hotshot.tables.ActiveInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Ziku on 2016-11-27.
 */

public class UniversalRefresh {

    public static final int lighterOrangeColor = Color.parseColor("#FFCC99");
    public static final int orangeColor = Color.parseColor("#FF6600");
    public static final String LAST_DB_UPDATE = "LastDBUpdate";
    public static final String LAST_APK_VERSION = "LastAPKVersion";
    public static final String LAST_HOTSHOT_UPDATE = "LastHotShotUpdate";
    public static final String APP_VERSION = "AppVersion";
    public static final long HOURS_24 = 24 * 60 * 60 * 1000;
    public static final long HOURS_72 = 3 * 24 * 60 * 60 * 1000;
    public static final long HOUR = 70 * 60 * 1000;
    public static final String POBIERANIE_INFORMACJI = "HotShot: Odświeżanie informacji";
    public static final String INTERNET_ERROR = "Problem połączenia z internetem, oferty mogą się nie wyświetlać lub być nieaktualne.";
    public static final String SHARED_PREFERENCES = "hotSthoSharedPref";

    public static void RefreshCategoriesAndWebPages(final Context context){

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
                        JsonCategoriesAsync jsonCategoriesAsync = new JsonCategoriesAsync(context);
                        JsonWebPagesAsync jsonWebPagesAsync = new JsonWebPagesAsync(context);
                        jsonCategoriesAsync.UpdateAllCategories();
                        jsonWebPagesAsync.UpdateAllWebPages();
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

    public static void UniwersalRefreshById(Activity activity, ListView listView, int selectedList) {
        if (listView != null) {
            List<ActiveHotShots> activeWebSitesList = ActiveHotShots.ReturnAllActiveHotShotsActive(selectedList);
            HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(activity.getBaseContext(), activeWebSitesList);
            listView.setAdapter(null);
            listView.setAdapter(hotShotsAdapter);
            Log.d("LIST", "list is not null");
        }
    }

    public static void RefreshAllIfNoLongedRefreshed(final Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(UniversalRefresh.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        long lastCheck = sharedPreferences.getLong(LAST_HOTSHOT_UPDATE,0);
        long currentTime = System.currentTimeMillis();

//        ActiveHotShots activeHotShot =

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {

            if (networkInfo.isConnectedOrConnecting()) {
                Log.d("TEST", "Connected or connecting");

                Date last = new Date(lastCheck);
                Date current = new Date(currentTime);
                Log.d("REF", "last: " + last.toString() + " current: " + current.toString());
                if (lastCheck == 0 || ((currentTime - lastCheck) > HOUR)) {
                    Log.d("REF", "HotShots need to be refreshed");
                    AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                        @Override
                        public void run() {
                            UniversalRefresh.RefreshCategoriesAndWebPages(activity.getApplicationContext());
                            UniversalRefresh.RefreshAllIfPossible(activity);
                        }
                    }, activity.getBaseContext(), true);
//                    ShowToasWithMessage(activity.getApplicationContext(), POBIERANIE_INFORMACJI);
                    hotShotsAsync.execute();
                } else {
                    Log.d("REF", "HotShots up to date");
                }
            }
        }
    }

    public static void AlerDialogWithMessage(String message, Context context){
        try {
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
        }catch (Exception ex){}
    }

    public static void UpdateDialogShow(final Context context){
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Nowa wersja");
            alertDialog.setMessage("Nowsza wersja aplikacji jest dostępna do pobrania.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aktualizuj", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent goToShopIntent = new Intent(Intent.ACTION_VIEW,uri);

                    try{
                        context.startActivity(goToShopIntent);
                    } catch (ActivityNotFoundException ex){
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                    }
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Pomiń", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }catch (Exception ex){}
    }

    public static void RemoveAllNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void ShowToasWithMessage(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void CheckForNewAPKVersion(Context context){
//        int currentVersion;
//        try{
//            currentVersion=  context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
//        } catch (PackageManager.NameNotFoundException ex){
//            currentVersion = 0;
//        }
//
//        Log.d("TEST","Wersja: " + String.valueOf(currentVersion));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        long lastCheck = sharedPreferences.getLong(LAST_APK_VERSION,0);
        final long currentTime = System.currentTimeMillis();

        CheckForUpdates checkForUpdates = new CheckForUpdates(context, new Runnable() {
            @Override
            public void run() {
                editor.putLong(LAST_APK_VERSION,currentTime).apply();
            }
        });

        if(lastCheck == 0){
            checkForUpdates.execute();
        } else if(currentTime - lastCheck > HOURS_72){
            checkForUpdates.execute();
        } else {Log.d("TEST","DB up to date");}
    }

}