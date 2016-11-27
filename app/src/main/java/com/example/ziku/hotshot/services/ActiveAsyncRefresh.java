package com.example.ziku.hotshot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;

/**
 * Created by Ziku on 2016-09-14.
 */
public class ActiveAsyncRefresh extends AsyncTask<String, Void, Void>{

    private Context context;
//    private ActiveWebPageFabric activeWebPageFabric;
    private Runnable postExecute = null;
    private boolean forced;

    public ActiveAsyncRefresh(Context context,Runnable postExecute,boolean forced){
        this.context = context;
//        this.activeWebPageFabric = new ActiveWebPageFabric(context);
        this.postExecute = postExecute;
        this.forced = forced;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Notification notification;
        Notification notificationToSend = null;

        for (String webpage : strings) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            Log.d("TEST","Processing inside function");

            if(networkInfo != null) {

                if (networkInfo.isConnectedOrConnecting()) {
                    Log.d("TEST", "Connected or connecting");
                    Log.d("TEST", "Processing " + webpage);
                  //  notification = activeWebPageFabric.UpgradeSomeHotShot(webpage, forced);

//                    if (notification != null) {
//                        try {
//                            notificationToSend = notification;
//                        } catch (Exception ex) {
//                            Log.d("TEST", webpage + " cos sie spierdolilo: " + ex);
//                        }
//                    }
                } else {Log.d("TEST","No internet connection");}
            } else {Log.d("TEST","Network info is null");}
        }

        if(notificationToSend != null && !forced){
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1,notificationToSend);
        }
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
