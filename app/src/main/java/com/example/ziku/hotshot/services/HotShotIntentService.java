package com.example.ziku.hotshot.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.ziku.hotshot.jsonservices.JsonHotShotsAsync;

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
        Log.d("TEST0","intent");
        JsonHotShotsAsync jsonHotShotsAsync = new JsonHotShotsAsync(getApplicationContext(),false);
        jsonHotShotsAsync.ExecuteJsonRefreshing();
        HotShotAlarmReceiver.sleepBitch();
    }
}
