package com.example.ziku.hotshot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.ziku.hotshot.HotShotsDatabase;
import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.WebPageFabric;
import com.example.ziku.hotshot.tables.HotShotsTable;
import com.example.ziku.hotshot.tables.WebSiteTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ziku on 2016-07-31.
 */
public class HotShotAsyncTast extends AsyncTask<String,Void,Void>{

    private static long MAX_BITMAT_SIZE = 1_000_000;

    private HotShotsDatabase db;
    private Context context;
    private ConnectivityManager connectivityManager;
    private Runnable postExecute = null;
    private boolean imageForced;
    private PowerManager powerManager;
    private PowerManager.WakeLock powerWakeLock;

    public HotShotAsyncTast(HotShotsDatabase db , Context context, ConnectivityManager connectivityManager, Runnable postExecute, boolean imageForced)
    {

        super();
        this.db = db;
        this.context = context;
        this.connectivityManager = connectivityManager;
        this.postExecute = postExecute;
        this.imageForced = imageForced;
//        this.powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
//        this.powerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"HotShotAsyncTask");

    }

    @Override
    protected Void doInBackground(String... strings) {

//        HotShotAlarmReceiver.wakeUpMothaFucka(context);
//        powerWakeLock.acquire();

        Notification notification;
        Notification notificationToSend = null;

        for (String webpage : strings) {
            Log.d("TEST","Processing " + webpage);
            notification = DoUpgradeOfProduct(webpage);
            if(notification!=null){
                try {
                    notificationToSend = notification;
                }
                catch (Exception ex){
                    Log.d("TEST",webpage + " cos sie spierdolilo: " + ex);
                }
            }
        }

        if(notificationToSend != null){
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1,notificationToSend);
        }
//        HotShotAlarmReceiver.sleepBitch();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(postExecute!=null){
            postExecute.run();
        }
        Log.d("TEST","Post execute Async Task");
    }

    private Notification DoUpgradeOfProduct(String webPage)
    {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("TEST","Processing inside function");
//        nm.notify(1, CreateNotification("processing","processing", context));
//        nm.notify(1, CreateNotification("inside","inside", context));
        if(networkInfo != null) {
            if(networkInfo.isConnectedOrConnecting()) {
                Log.d("TEST","Connected or connecting");
//                nm.notify(1, CreateNotification("inside","inside", context));
                String before = db.GetProductName(webPage);
                db.UpdateHotShot(webPage);
                String after = db.GetProductName(webPage);
                if(!before.equals(after) && !after.equals(WebPageFabric.EMPTY)) {
                    db.UpdateDateCheckOfHotShot(webPage);
                    Cursor cursor = db.GetImageAndName(webPage);
                    cursor.moveToFirst();
                    DownloadImages(cursor);
                    if (postExecute==null) {
                        String productName = cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.PRODUCT_NAME));
                        notification = CreateNotification(webPage, productName, context);
                    }
                } else if(imageForced){
                    Cursor cursor = db.GetImageAndName(webPage);
                    cursor.moveToFirst();
                    DownloadImages(cursor);
                }

            } else {Log.d("TEST","No internet connection");}
        } else {Log.d("TEST","Network info is null");}
        return notification;
    }

    public void DownloadImages( Cursor cursor){
        cursor.moveToFirst();
        String imgName = cursor.getString(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NAME));
        String imgUrl = cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.IMG_URL));
        DownloadTheIMG(imgName,imgUrl);
    }

    public void DownloadTheIMG(String imgName, String imgUrlString){
        Log.d("TEST","Start downloading image");

        try{
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgUrlString).getContent());

            Log.d("TEST",imgUrlString);
            String fileName = imgName + ".png";
            File file = context.getFileStreamPath(fileName);
            if(file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                 Log.d("TEST",String.valueOf(bitmap.getByteCount()));
                 if(bitmap.getByteCount()>(int)MAX_BITMAT_SIZE) {
                     bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.1),(int)(bitmap.getHeight()*0.1), true);
                 }
                   bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            }
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (IOException ex){
            Log.d("ERROR",ex.toString());
        }catch (NullPointerException ex){
            Log.d("ERROR", ex.toString());
        }
        Log.d("TEST","Imgage downloaded");
    }

    public Notification CreateNotification(String webPage, String product, Context context) {

        Intent hotShotOpen = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(hotShotOpen);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.hot_shot_orange);
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + " oferuje nową gorącą okazję: " + product);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        long[] steps = {0, 300, 250, 200, 100, 150};
        notification.vibrate = steps;

        return notification;
    }
}
