package com.example.ziku.hotshot.tables;

import android.util.Log;

import com.activeandroid.query.Select;

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
    public final static String EMPTY = "-";

    public static void AddWebsitesIfNotExists(){
        Log.d("ACTIVE","try to add webpages");
        SaveWebSite(X_KOM,X_KOM_URL,true,true);
        SaveWebSite(KOMPUTRONIK,KOMPUTRONIK_URL,true,true);
        SaveWebSite(MORELE,MORELE_URL,true,true);
        SaveWebSite(SATYSFAKCJA,SATYSFAKCJA_URL,true,true);
        SaveWebSite(PROLINE,PROLINE_URL,true,true);
    }

    private static void SaveWebSite(String webSiteName, String webSiteUrl, boolean isActive, boolean notifyUser){
        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).where("web_site_name = ?",webSiteName).execute();
        if(activeWebSitesList.isEmpty()){
            ActiveWebSites activeWebSites = new ActiveWebSites(webSiteName,webSiteUrl,isActive,notifyUser);
            activeWebSites.save();
            Log.d("ACTIVE","adding new website " + webSiteName);
        } else {Log.d("ACTIVE","WebSite already exists " + webSiteName);}

    }
}
