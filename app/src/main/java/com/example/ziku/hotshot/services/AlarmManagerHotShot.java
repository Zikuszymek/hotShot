package com.example.ziku.hotshot.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ziku on 2016-08-26.
 */
public class AlarmManagerHotShot extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        int TIMER = 5 * 60 * 1000;
        long PERIOD = 2 * 60 * 1000;

        AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, HotShotAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD, TIMER, pendingIntent);
        Log.d("TEST", "start service");
    }

}
