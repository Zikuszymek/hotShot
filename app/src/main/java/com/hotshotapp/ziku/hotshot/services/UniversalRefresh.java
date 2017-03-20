package com.hotshotapp.ziku.hotshot.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.hotshotapp.ziku.hotshot.BuildConfig;
import com.hotshotapp.ziku.hotshot.MainActivity;
import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.AsyncTaskHotShot;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;
import com.hotshotapp.ziku.hotshot.jsonservices.RESTRequestAndCallback;
import com.hotshotapp.ziku.hotshot.jsonservices.RetrofitRequestHotshot;
import com.hotshotapp.ziku.hotshot.tables.ActiveInfo;


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
    public static final String INTERNET_ERROR = "Problem połączenia z internetem, oferty mogą się nie wyświetlać lub być nieaktualne.";
    public static final String SHARED_PREFERENCES = "hotSthoSharedPref";

    public static void RefreshCategoriesAndWebPages(final Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {

            if (networkInfo.isConnectedOrConnecting()) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                long lastCheck = sharedPreferences.getLong(LAST_DB_UPDATE, 0);
                final long currentTime = System.currentTimeMillis();

                AsyncTask<Void, Void, Void> refreshCategories = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        RESTRequestAndCallback restRequestAndCallback = new RESTRequestAndCallback(RetrofitRequestHotshot.getRetrofitAPI(), context);
                        restRequestAndCallback.getAllCategories();
                        restRequestAndCallback.getAllWebPages();
                        editor.putLong(LAST_DB_UPDATE, currentTime).apply();
                        return null;
                    }
                };

                if (lastCheck == 0 || (currentTime - lastCheck > HOURS_24)) {
                    refreshCategories.execute();
                } else {
                }
            } else {
                AlerDialogWithMessage(INTERNET_ERROR, context);
            }
        } else {
            AlerDialogWithMessage(INTERNET_ERROR, context);
        }
    }

    public static void AddNewGlobalInformationIfDoesNotExist(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String appVersion = sharedPreferences.getString(APP_VERSION, "");
        String currentAppVersion = BuildConfig.VERSION_NAME;


        if (!appVersion.equals(currentAppVersion)) {
            ActiveInfo.AddAllinterestingElement();
            editor.putString(APP_VERSION, currentAppVersion).apply();
        }
    }

    public static void RefreshAllIfNoLongedRefreshed(final Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(UniversalRefresh.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        long lastCheck = sharedPreferences.getLong(LAST_HOTSHOT_UPDATE, 0);
        long currentTime = System.currentTimeMillis();

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnectedOrConnecting()) {
                if (lastCheck == 0 || ((currentTime - lastCheck) > HOUR)) {
                    AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                        @Override
                        public void run() {
                            UniversalRefresh.RefreshCategoriesAndWebPages(activity.getApplicationContext());
                        }
                    }, activity.getBaseContext(), true);
                    hotShotsAsync.execute();
                } else {
                    Log.d("REF", "HotShots up to date");
                }
            }
        }
    }

    public static void AlerDialogWithMessage(String message, Context context) {
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
        } catch (Exception ex) {
        }
    }

    public static void UpdateDialogShow(final Context context) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Nowa wersja");
            alertDialog.setMessage("Nowsza wersja aplikacji jest dostępna do pobrania.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aktualizuj", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent goToShopIntent = new Intent(Intent.ACTION_VIEW, uri);

                    try {
                        context.startActivity(goToShopIntent);
                    } catch (ActivityNotFoundException ex) {
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
        } catch (Exception ex) {
        }
    }

    public static void RemoveAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public static void CheckForNewAPKVersion(Context context) {
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
        long lastCheck = sharedPreferences.getLong(LAST_APK_VERSION, 0);
        final long currentTime = System.currentTimeMillis();

        CheckForUpdates checkForUpdates = new CheckForUpdates(context, new Runnable() {
            @Override
            public void run() {
                editor.putLong(LAST_APK_VERSION, currentTime).apply();
            }
        });

        if (lastCheck == 0) {
            checkForUpdates.execute();
        } else if (currentTime - lastCheck > HOURS_72) {
            checkForUpdates.execute();
        } else {
            Log.d("TEST", "DB up to date");
        }
    }

    public static Notification CreateNotification(String webPage, String product, Context context) {

        Intent hotShotOpen = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(hotShotOpen);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            notificationBuilder.setSmallIcon(R.drawable.hot_shot_orange);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.hot_shot_icon);
        }
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + ": " + product);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        if (SharedSettingsHS.GetPreferenceBoolen(context.getString(R.string.key_vibration), context)) {
            long[] steps = {0, 150};
            notification.vibrate = steps;
        }
        return notification;
    }
}
