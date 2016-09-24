package com.example.ziku.hotshot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;

/**
 * Created by Ziku on 2016-08-24.
 */
public class HotShotAlarmReceiver extends BroadcastReceiver{
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.d("TEST0","receiver");
//        Intent service = new Intent(context,HotShotService.class);
//        startWakefulService(context,service);
//    }

    private static PowerManager.WakeLock wakelock = null;
    private static WifiManager.WifiLock wifiLock = null;

    public static synchronized void wakeUpMothaFucka(Context context){
        if(wakelock == null){
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakelock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"HOTSHOT");
            wakelock.setReferenceCounted(true);
        }
        wakelock.acquire();

        if(wifiLock == null){
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            if(!wifiManager.isWifiEnabled()) {
//                wifiManager.setWifiEnabled(true);
//            }
            wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"HOTSHOT");
        }
        wifiLock.acquire();
    }

    public static synchronized void sleepBitch(){
//        if(wakelock.isHeld())
//            Log.d("TEST","wakelock is active");
//        if(wifiLock.isHeld())
//            Log.d("TEST","wikilock is active");

        if(wakelock!=null) {
            wakelock.release();
            wakelock = null;
        }
        if(wifiLock!=null) {
            wifiLock.release();
            wifiLock = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wakeUpMothaFucka(context);
        Intent service = new Intent(context,HotShotService.class);
        context.startService(service);
    }

}
