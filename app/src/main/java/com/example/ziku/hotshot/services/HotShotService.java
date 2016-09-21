package com.example.ziku.hotshot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.WebPageFabric;
import com.example.ziku.hotshot.tables.ActiveORMmanager;

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
        PowerManager.WakeLock wakelock;
        WifiManager.WifiLock wifiLock;

//        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakelock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"HOTSHOT");
//        wakelock.acquire();
//
//        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL,"HOTSHOT");
//        wifiLock.acquire();


//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        HotShotAlarmReceiver.wakeUpMothaFucka(this);
        SendNotification("cos","cos",this);

        Log.d("TEST","service started to run");
        try {
            ActiveAsyncRefresh activeAsyncRefresh = new ActiveAsyncRefresh(this,null,false);
            cos = activeAsyncRefresh.execute(ActiveORMmanager.X_KOM, ActiveORMmanager.KOMPUTRONIK, ActiveORMmanager.SATYSFAKCJA,
                        ActiveORMmanager.MORELE, ActiveORMmanager.PROLINE, ActiveORMmanager.HELION).get();
        }catch (Exception ex){
            Log.d("TEST","DUPA: "+ ex);
        }
        Log.d("TEST","SERVICE EXIT");

//        wakelock.release();
//        wifiLock.release();
        HotShotAlarmReceiver.completeWakefulIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void SendNotification(String webPage, String product, Context context){

        Intent hotShotOpen = new Intent(context, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(hotShotOpen);
        hotShotOpen.setAction(Intent.ACTION_MAIN);
        hotShotOpen.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,hotShotOpen,0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.hot_shot_orange);
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + " oferuje nową gorącą okazję: " + product);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        long[] steps = {0, 300, 250, 200, 100, 150};
        notification.vibrate = steps;

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1,notification);
    }
}
