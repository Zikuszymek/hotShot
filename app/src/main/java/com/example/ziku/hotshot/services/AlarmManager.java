package com.example.ziku.hotshot.services;

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
public class AlarmManager extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "DUPA", Toast.LENGTH_LONG).show();
        int TIMER = 60 * 60 * 1000;
        long PERIOD = 2 * 60 * 1000;

        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, HotShotAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD, TIMER, pendingIntent);
        Log.d("TEST", "start service");
    }

    public static void SetAlarmManager(Context context){
        Log.d("TEST", "check if service can be started");

        long TIMER = 60 * 60 * 1000;
        long PERIOD = 5 * 60 * 1000;
        long PROPER_START_TIME = System.currentTimeMillis() + PERIOD;
//            long PROPER_START_TIME = (System.currentTimeMillis()-(Calendar.getInstance().get(Calendar.MINUTE)*60*1000)) + PERIOD;

        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, HotShotAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, serviceIntent, 0);

        manager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, PROPER_START_TIME, TIMER, pendingIntent);
        Log.d("TEST", "start service" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(PROPER_START_TIME)));
    }
}
