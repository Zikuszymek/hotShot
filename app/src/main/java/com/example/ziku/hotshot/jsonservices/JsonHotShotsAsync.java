package com.example.ziku.hotshot.jsonservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.services.UniversalRefresh;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


/**
 * Created by Ziku on 2016-11-19.
 */

public class JsonHotShotsAsync {

    private static final String TAG = JsonHotShotsAsync.class.getSimpleName();
    private static final String WEB_URL = "http://hotshot.ziku.ayz.pl/hotshots/?format=json";
    private static long MAX_BITMAT_SIZE = 1_000_000;
    private Context context;
    private boolean forced;

    public JsonHotShotsAsync(Context context, boolean forced){
        this.context = context;
        this.forced = forced;
    }

    public void ExecuteJsonRefreshing(){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {

            if (networkInfo.isConnectedOrConnecting()) {
                Log.d("TEST", "Connected or connecting");
                JsonHttp jsonHttp = new JsonHttp();
                String jsonResponse = jsonHttp.JsonServiceCall(WEB_URL);
                Log.d(TAG, jsonResponse);
                if (jsonResponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        Notification notificationToSend = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String idHotShot = object.getString("id_hot_shot");
                            String productName = object.getString("product_name");
                            String oldPrice = object.getString("old_price");
                            String newPrice = object.getString("new_price");
                            String lastCheck = object.getString("last_check");
                            String productUrl = object.getString("product_url");
                            String imbUrl = object.getString("img_url");
                            String webPage = object.getString("web_page");

                            Log.d(TAG, idHotShot);
                            Log.d(TAG, productName);
                            Log.d(TAG, oldPrice);
                            Log.d(TAG, newPrice);
                            Log.d(TAG, lastCheck);
                            Log.d(TAG, productUrl);
                            Log.d(TAG, imbUrl);
                            Log.d(TAG, webPage);

                            Date now = new Date();

                            ActiveWebSites activeWebSite = ActiveWebSites.load(ActiveWebSites.class, Integer.parseInt(webPage));
                            if (activeWebSite != null) {
                                ActiveHotShots activeHotShot = ActiveHotShots.load(ActiveHotShots.class, Integer.parseInt(idHotShot));
                                if (activeHotShot == null) {
                                    ActiveHotShots newActiveHotShot = new ActiveHotShots(productName, Integer.parseInt(oldPrice), Integer.parseInt(newPrice),
                                            imbUrl, productUrl, activeWebSite, productName, now);
                                    newActiveHotShot.save();

                                    DownloadTheIMG(activeWebSite.webSiteName + String.valueOf(newActiveHotShot.getId()), newActiveHotShot.imgUrl);
                                    notificationToSend = CreateNotification(activeWebSite.webSiteName,newActiveHotShot.productName,context);

                                    Log.d(TAG, "new hotshot");
                                } else {
                                    activeHotShot.productName = productName;
                                    activeHotShot.oldPrice = Integer.parseInt(oldPrice);
                                    activeHotShot.newPrice = Integer.parseInt(newPrice);
                                    activeHotShot.imgUrl = imbUrl;
                                    activeHotShot.productUrl = productUrl;
                                    activeHotShot.webSites = activeWebSite;

                                    if (!activeHotShot.productName.equals(JsonUpdateDB.EMPTY) && !activeHotShot.productName.equals(activeHotShot.lastNotyfication)) {

                                        DownloadTheIMG(activeWebSite.webSiteName + String.valueOf(activeHotShot.getId()), activeHotShot.imgUrl);
                                        activeHotShot.lastNotyfication = productName;
                                        activeHotShot.lastNewChange = now;

                                        notificationToSend = CreateNotification(activeWebSite.webSiteName,activeHotShot.productName,context);
                                    }
                                    activeHotShot.save();

                                    Log.d(TAG, "upgrade hotshot");
                                }
                            }
                        }

                        if(notificationToSend != null && !forced){
                            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify(1,notificationToSend);
                        }

                    } catch (JSONException ex) {
                        UniversalRefresh.AlerDialogWithMessage(UniversalRefresh.INTERNET_ERROR,context);
                        Log.d(TAG, "JsonException " + ex.toString());
                    }
                } else {
                    UniversalRefresh.AlerDialogWithMessage(UniversalRefresh.INTERNET_ERROR,context);
                    Log.d(TAG, "Could not get json from");
                }
            } else {
                UniversalRefresh.AlerDialogWithMessage(UniversalRefresh.INTERNET_ERROR,context);
                Log.d("TEST","No internet connection");
            }
        } else {
            UniversalRefresh.AlerDialogWithMessage(UniversalRefresh.INTERNET_ERROR,context);
            Log.d("TEST","Network info is null");
        }
    }

    private void DownloadTheIMG(String imgName, String imgUrlString){
        Log.d("BITMAP","Start downloading image");
        try{
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgUrlString).getContent());
            Log.d("BITMAP",imgUrlString);
            String fileName = imgName + ".png";
            File file = context.getFileStreamPath(fileName);
            if(file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Log.d("BITMAP",String.valueOf(bitmap.getByteCount()));
            if(bitmap.getByteCount()>(int)MAX_BITMAT_SIZE) {
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.5),(int)(bitmap.getHeight()*0.5), true);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (IOException ex){
            Log.d("BITMAP",ex.toString());
        }catch (NullPointerException ex){
            Log.d("BITMAP", ex.toString());
        }
        Log.d("BITMAP","Imgage downloaded");
    }

    public Notification CreateNotification(String webPage, String product, Context context) {

        Intent hotShotOpen = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(hotShotOpen);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.hot_shot_icon);
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + " " + product);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        long[] steps = {0, 300, 250, 200, 100, 150};
        notification.vibrate = steps;

        return notification;
    }
}
