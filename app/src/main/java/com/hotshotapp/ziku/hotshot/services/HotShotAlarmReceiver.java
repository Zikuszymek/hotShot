package com.hotshotapp.ziku.hotshot.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Ziku on 2016-08-24.
 */
public class HotShotAlarmReceiver extends BroadcastReceiver{

    private static int REQUEST_CODE = 12345;

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
            wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"HOTSHOT");
        }
        wifiLock.acquire();
    }

    public static synchronized void sleepBitch(){

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
        Intent service = new Intent(context,HotShotIntentService.class);
        context.startService(service);
        SetAlarmManager(context);
    }

    public static void SetAlarmManager(Context context){
        Log.d("TEST","setting alarm");
        long PERIOD = 4 * 60 * 1000;
//        long PERIOD2 = 60 * 1000;
        long TIMER = 60 * 60 * 1000;
        long PROPER_START_TIME = (System.currentTimeMillis()-(Calendar.getInstance().get(Calendar.MINUTE)*60*1000)) + (PERIOD) + TIMER;
//        long PROPER_START_TIME = System.currentTimeMillis() + PERIOD2;
        SetProperAlarmManager(context, PROPER_START_TIME);
    }

    public static void SetProperAlarmManager(Context context, long PROPER_START_TIME){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,HotShotAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE,intent,0);
        alarmManager.cancel(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,PROPER_START_TIME,pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,PROPER_START_TIME,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,PROPER_START_TIME,pendingIntent);
        }
    }

    public static void CancelAlarmManager(Context context){
        AlarmManager alarmToCancel = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,HotShotAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE,intent,0);
        alarmToCancel.cancel(pendingIntent);
    }
}
