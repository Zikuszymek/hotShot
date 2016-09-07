package com.example.ziku.hotshot.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;

/**
 * Created by Ziku on 2016-08-26.
 */
public class HotShotIntentService extends IntentService{

    public HotShotIntentService(){
        super("HotShotIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        HotShotAlarmReceiver.wakeUpMothaFucka(this);
        Toast.makeText(this, "DUPA", Toast.LENGTH_LONG).show();
        SendNotification("cos","cos",this);

//        for(int i = 0;i<100;i++){
//            try{
//                Thread.sleep(1000*30);
//                Toast.makeText(this, "DUPA", Toast.LENGTH_LONG).show();
//            }catch (Exception ex){}
//        }
    }

    public static void SendNotification(String webPage, String product, Context context){

        Intent hotShotOpen = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(hotShotOpen);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.hot_shot_orange);
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + " oferuje nową gorącą okazję: " + product);
        notificationBuilder.setContentIntent(pendingIntent);

        Notification notification = notificationBuilder.build();
        long[] steps = {0,300,250,200,100,150};
        notification.vibrate = steps;

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1,notification);
    }
}
