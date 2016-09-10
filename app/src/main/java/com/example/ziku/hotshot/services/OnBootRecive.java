package com.example.ziku.hotshot.services;

import android.app.*;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ziku.hotshot.MainActivity;


/**
 * Created by Ziku on 2016-09-04.
 */
public class OnBootRecive extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TEST","recievied");
//        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
//            Intent intent1 = new Intent(context,BackgroundService.class);
////            context.startService(intent1);
//            Intent intent1 = new Intent(context,BackgroundService.class);
//            PendingIntent pendingIntent = PendingIntent.getService(context,0,intent1,0);
//            int interval = 8000;
//            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval,pendingIntent);
//        }
    }
}
