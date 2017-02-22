package com.hotshotapp.ziku.hotshot.tables;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.KeyItems;
import com.hotshotapp.ziku.hotshot.management.ImageAndFiles.ImageAndFilesManager;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;
import com.hotshotapp.ziku.hotshot.services.UniversalRefresh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by Ziku on 2017-02-22.
 */

public class HotShotsGSONConverter {

    private int id_hot_shot;
    private String product_name;
    private int old_price;
    private int new_price;
    private String last_check;
    private String product_url;
    private String img_url;
    private int web_page;

    public HotShotsGSONConverter() {
    }

    public int getId_hot_shot() {
        return id_hot_shot;
    }

    public void setId_hot_shot(int id_hot_shot) {
        this.id_hot_shot = id_hot_shot;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getOld_price() {
        return old_price;
    }

    public void setOld_price(int old_price) {
        this.old_price = old_price;
    }

    public int getNew_price() {
        return new_price;
    }

    public void setNew_price(int new_price) {
        this.new_price = new_price;
    }

    public String getLast_check() {
        return last_check;
    }

    public void setLast_check(String last_check) {
        this.last_check = last_check;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getWeb_page() {
        return web_page;
    }

    public void setWeb_page(int web_page) {
        this.web_page = web_page;
    }

    public Notification convertToActiveHotShot(Context context) {

        Date now = new Date();
        Notification notificationToSend = null;
        ImageAndFilesManager imageAndFilesManager = new ImageAndFilesManager(context);

        ActiveWebSites activeWebSite = ActiveWebSites.load(ActiveWebSites.class, getWeb_page());
        if (activeWebSite != null) {
            ActiveHotShots activeHotShot = ActiveHotShots.load(ActiveHotShots.class, getId_hot_shot());

            if (activeHotShot == null) {
                ActiveHotShots newActiveHotShot = new ActiveHotShots(getProduct_name(), old_price, new_price,
                        getImg_url(), getProduct_url(), activeWebSite, getProduct_name(), now);
                newActiveHotShot.save();

                    imageAndFilesManager.downloadImageForNewHotShotAndSaveIt(activeWebSite.webSiteName, String.valueOf(newActiveHotShot.getId()), newActiveHotShot.imgUrl);
                    notificationToSend = UniversalRefresh.CreateNotification(activeWebSite.webSiteName, newActiveHotShot.productName, context);
            }

            else {
                activeHotShot.productName = getProduct_name();
                activeHotShot.oldPrice = getOld_price();
                activeHotShot.newPrice = getNew_price();
                activeHotShot.imgUrl = getImg_url();
                activeHotShot.productUrl = getProduct_url();
                activeHotShot.webSites = activeWebSite;

                if (!activeHotShot.productName.equals(KeyItems.EMPTY) && !activeHotShot.productName.equals(activeHotShot.lastNotyfication)) {

                    imageAndFilesManager.downloadImageForNewHotShotAndSaveIt(activeWebSite.webSiteName, String.valueOf(activeHotShot.getId()),activeHotShot.imgUrl);
                    activeHotShot.lastNotyfication = getProduct_name();
                    activeHotShot.lastNewChange = now;

                    if (activeHotShot.webSites.notifyUser) {
                        notificationToSend = UniversalRefresh.CreateNotification(activeWebSite.webSiteName, activeHotShot.productName, context);
                    }
                }

                boolean haveImageDownloaded = imageAndFilesManager.checkIfHotShotHaveImageDownloaded(activeWebSite.webSiteName,String.valueOf(activeHotShot.getId()));
                if(!haveImageDownloaded) {
                    imageAndFilesManager.downloadImageForNewHotShotAndSaveIt(activeWebSite.webSiteName, String.valueOf(activeHotShot.getId()),activeHotShot.imgUrl);
                }
                activeHotShot.save();
            }
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(UniversalRefresh.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long currentTime = System.currentTimeMillis();
        editor.putLong(UniversalRefresh.LAST_HOTSHOT_UPDATE, currentTime).apply();

        return notificationToSend;
    }
}

