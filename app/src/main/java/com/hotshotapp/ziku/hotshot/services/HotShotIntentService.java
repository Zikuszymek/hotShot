package com.hotshotapp.ziku.hotshot.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.JsonHotShotsAsync;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;

/**
 * Created by Ziku on 2016-11-30.
 */

public class HotShotIntentService extends IntentService{

    public static String intentName = "HotshotIntent";

    public HotShotIntentService() {
        super(intentName);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean onlyWithWifi = SharedSettingsHS.GetPreferenceBoolen(getApplicationContext().getString(R.string.key_sync_wifi),getApplicationContext());
        boolean canSynchronized = true;

        if(onlyWithWifi){
            canSynchronized = false;
            WifiManager wifiManager = (WifiManager)  getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiManager != null && wifiManager.isWifiEnabled()) {
                canSynchronized = true;
            }
        }
        if(canSynchronized) {
            JsonHotShotsAsync jsonHotShotsAsync = new JsonHotShotsAsync(getApplicationContext(), false);
            jsonHotShotsAsync.ExecuteJsonRefreshing();
        }
        HotShotAlarmReceiver.sleepBitch();
    }
}
