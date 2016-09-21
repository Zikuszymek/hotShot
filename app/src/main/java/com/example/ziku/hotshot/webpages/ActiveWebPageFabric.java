package com.example.ziku.hotshot.webpages;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.activeandroid.query.Select;
import com.example.ziku.hotshot.MainActivity;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveORMmanager;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ziku on 2016-07-11.
 */
public class ActiveWebPageFabric {

    private static long MAX_BITMAT_SIZE = 1_000_000;
    private Context context;

    public ActiveWebPageFabric(Context context){
        this.context = context;
    }

    public Notification UpgradeSomeHotShot(String hotShotWebSite, boolean forced){
        Notification notification = null;
        List<ActiveWebSites> activeWebSite = new Select().from(ActiveWebSites.class).where(ActiveWebSites.WEB_SITE_NAME + " = ?",hotShotWebSite).execute();
        ActiveWebSites thisActiveWebSite = activeWebSite.get(0);
       
        long timePeriod = thisActiveWebSite.timePeriod;
        Date lastCheck = thisActiveWebSite.lastCheck;
        Date now = new Date();
        long differenceBetweenDates = now.getTime() - lastCheck.getTime();
//        if(forced || differenceBetweenDates > timePeriod){

            List<ActiveHotShots> activeHotShotsList = new Select().from(ActiveHotShots.class).where(ActiveHotShots.WEB_SITE_ID + " = ?",thisActiveWebSite.getId()).execute();
            if(!activeHotShotsList.isEmpty()){

                ArrayList<ContentValues> cv = returnNewWebSiteData(thisActiveWebSite.webSiteName);
                for (ActiveHotShots thisHotShot : activeHotShotsList) {
                    String productNameBefore = thisHotShot.productName;
                    ContentValues contentValues = cv.get(thisHotShot.hotShotWebId);
                    String newProductName = contentValues.getAsString(ActiveHotShots.PRODUCT_NAME);

//                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    nm.notify(1, CreateNotification("inside","inside", context));

                    thisHotShot.productName = newProductName;
                    thisHotShot.oldPrice = contentValues.getAsInteger(ActiveHotShots.OLD_PRICE);
                    thisHotShot.newPrice = contentValues.getAsInteger(ActiveHotShots.NEW_PRICE);
                    thisHotShot.imgUrl = contentValues.getAsString(ActiveHotShots.IMG_URL);
                    thisHotShot.productUrl = contentValues.getAsString(ActiveHotShots.PRODUCT_URL);
//                    thisActiveWebSite.save();

                    if(newProductName!=null && !newProductName.equals(productNameBefore) && !newProductName.equals(ActiveORMmanager.EMPTY)){

                        if(!thisHotShot.lastNotyfication.equals(newProductName) && thisActiveWebSite.notifyUser){
                            thisHotShot.lastNotyfication = newProductName;
                            notification = CreateNotification(thisActiveWebSite.webSiteName,thisHotShot.productName,context);
                        }
                        DownloadTheIMG(thisActiveWebSite.webSiteName,thisHotShot.imgUrl);

                        thisActiveWebSite.lastCheck = now;
                    }
                    thisHotShot.save();
                    thisActiveWebSite.save();
                }
//            }
        }
        return notification;
    }


    public void DownloadTheIMG(String imgName, String imgUrlString){
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
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.3),(int)(bitmap.getHeight()*0.3), true);
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

    public ArrayList<ContentValues> returnNewWebSiteData(String webSite)
    {
        WebPage webPage;
        switch (webSite){
            case ActiveORMmanager.X_KOM:
                webPage = new XkomWebPage();
                break;
            case ActiveORMmanager.KOMPUTRONIK:
                webPage = new KomputronikWebPage();
                break;
            case ActiveORMmanager.MORELE:
                webPage = new MoreleWebPage();
                break;
            case ActiveORMmanager.PROLINE:
                webPage = new ProlineWebPage();
                break;
            case ActiveORMmanager.SATYSFAKCJA:
                webPage = new SatysfakcjaWebPage();
                break;
            case ActiveORMmanager.HELION:
                webPage = new HelionWebPage();
                break;
            default:
                webPage = new XkomWebPage();
                break;
        }
        return webPage.GetWebPageData();
    }

    public Notification CreateNotification(String webPage, String product, Context context) {

        Intent hotShotOpen = new Intent(context, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(hotShotOpen);
        hotShotOpen.setAction(Intent.ACTION_MAIN);
        hotShotOpen.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,hotShotOpen,0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.hot_shot_orange);
        notificationBuilder.setContentTitle("HotShot");
        notificationBuilder.setContentText(webPage + " oferuje nową gorącą okazję: " + product);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        long[] steps = {0, 300, 250, 200, 100, 150};
        notification.vibrate = steps;

        return notification;
    }
}
