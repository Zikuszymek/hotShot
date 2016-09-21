package com.example.ziku.hotshot.tables;

import android.util.Log;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by Ziku on 2016-09-11.
 */
public class ActiveORMmanager {

    public final static String X_KOM = "X-kom";
    public final static String X_KOM_URL = "http://www.x-kom.pl/";
    public final static String KOMPUTRONIK = "Komputronik";
    public final static String KOMPUTRONIK_URL = "http://www.komputronik.pl/";
    public final static String PROLINE = "Proline";
    public final static String PROLINE_URL = "http://proline.pl/";
    public final static String MORELE = "Morele";
    public final static String MORELE_URL = "https://www.morele.net/";
    public final static String SATYSFAKCJA = "Satysfakcja";
    public final static String SATYSFAKCJA_URL = "http://www.satysfakcja.pl/";
    public final static String MALL = "Mall";
    public final static String MALL_URL = "https://www.mall.pl/";
    public final static String HELION = "Helion";
    public final static String HELION_URL = "http://helion.pl/";

    public final static String EMPTY = "-";
    public final static int NULL_PRICE = 0;

    public final static long HOURS_12 = 12 * 60 * 60 * 1000;
    public final static long HOURS_24 = 24 * 60 * 60 * 1000;


    public static void AddWebsitesIfNotExists(){
        Date yesterday = new Date(new Date().getTime() - HOURS_24);
        Log.d("ACTIVE","try to add webpages");
        SaveWebSite(X_KOM,X_KOM_URL,true,true,HOURS_12,yesterday);
        SaveWebSite(KOMPUTRONIK,KOMPUTRONIK_URL,true,true,HOURS_24,yesterday);
        SaveWebSite(MORELE,MORELE_URL,true,true,HOURS_24,yesterday);
        SaveWebSite(SATYSFAKCJA,SATYSFAKCJA_URL,true,true,HOURS_12,yesterday);
        SaveWebSite(PROLINE,PROLINE_URL,true,true,HOURS_12,yesterday);
//        SaveWebSite(MALL,MALL_URL,true,true,HOURS_24,yesterday);
        SaveWebSite(HELION,HELION_URL,true,true,HOURS_24,yesterday);
    }

    public static void AddStaticHotShots(){
        Log.d("ACTIVE","ADDINT HOTSHOTS ");
        SaveHotShot(ActiveHotShots.ID_0, X_KOM,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
        SaveHotShot(ActiveHotShots.ID_0, KOMPUTRONIK,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
        SaveHotShot(ActiveHotShots.ID_0, MORELE,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
        SaveHotShot(ActiveHotShots.ID_0, PROLINE,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
        SaveHotShot(ActiveHotShots.ID_0, SATYSFAKCJA,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
        SaveHotShot(ActiveHotShots.ID_0, HELION,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
//        SaveHotShot(ActiveHotShots.ID_0, MALL,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
//        SaveHotShot(ActiveHotShots.ID_1, MALL,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
//        SaveHotShot(ActiveHotShots.ID_2, MALL,EMPTY,NULL_PRICE,NULL_PRICE,EMPTY,EMPTY);
    }

    private static void SaveWebSite(String webSiteName, String webSiteUrl, boolean isActive, boolean notifyUser, long timePeriod, Date lastCheck){
        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).where(ActiveWebSites.WEB_SITE_NAME + " = ?",webSiteName).execute();
        if(activeWebSitesList.isEmpty()){
            ActiveWebSites activeWebSites = new ActiveWebSites(webSiteName,webSiteUrl,isActive,notifyUser, timePeriod, lastCheck);
            activeWebSites.save();
            Log.d("ACTIVE","adding new website " + webSiteName);
        } else {Log.d("ACTIVE","WebSite already exists " + webSiteName);}
    }

    private static void SaveHotShot(int webID, String webSiteName, String productName, int oldPrice, int newPrice, String imgUrl, String productUrl){
        List<ActiveWebSites> selectedWebSite = new Select().from(ActiveWebSites.class).where(ActiveWebSites.WEB_SITE_NAME + " = ?",webSiteName).execute();
        String lastNotyfication = EMPTY;
        if(!selectedWebSite.isEmpty()) {
            ActiveWebSites webSites = selectedWebSite.get(0);
            ActiveHotShots hotShots = new ActiveHotShots(webID, productName, oldPrice, newPrice, imgUrl, productUrl, webSites, lastNotyfication);
            hotShots.save();
            Log.d("ACTIVE","adding new hotShot " + webSiteName);
        }
    }
}
