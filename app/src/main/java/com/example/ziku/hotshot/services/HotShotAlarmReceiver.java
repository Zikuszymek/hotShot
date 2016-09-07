package com.example.ziku.hotshot.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

/**
 * Created by Ziku on 2016-08-24.
 */
public class HotShotAlarmReceiver extends BroadcastReceiver{

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
            wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"LOCK");
        }
        wifiLock.acquire();
    }

    public static synchronized void sleepBitch(){
        if(wakelock!=null)
            wakelock.release();
        if(wifiLock!=null)
            wifiLock.release();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wakeUpMothaFucka(context);
        Intent service = new Intent(context,HotShotService.class);
        context.startService(service);
    }
}
