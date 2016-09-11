package com.example.ziku.hotshot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ziku.hotshot.HotShotsDatabase;
import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.WebPageFabric;
import com.example.ziku.hotshot.tables.HotShotsTable;
import com.example.ziku.hotshot.tables.WebSiteTable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ziku on 2016-07-31.
 */
public class HotShotService extends Service{


    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        HotShotService getService(){
            return HotShotService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    Void cos;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        HotShotAlarmReceiver.wakeUpMothaFucka(this);
//        SendNotification("cos","cos",this);

        Log.d("TEST","service started to run");
        try {
            HotShotsDatabase db = HotShotsDatabase.ReturnSingleInstance(this);

            HotShotAsyncTast asyncRefresh = new HotShotAsyncTast(db, this, connectivityManager, null, true);
            cos = asyncRefresh.execute(WebPageFabric.KOMPUTRONIK, WebPageFabric.X_KOM,
                    WebPageFabric.MORELE, WebPageFabric.PROLINE, WebPageFabric.SATYSFAKCJA).get();
        }catch (Exception ex){
            Log.d("TEST","DUPA: "+ ex);
        }
        Log.d("TEST","SERVICE EXIT");
        HotShotAlarmReceiver.sleepBitch();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void SendNotification(String webPage, String product, Context context){

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
        long[] steps = {0,300,250,200,100,150};
        notification.vibrate = steps;

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1,notification);
    }
}
