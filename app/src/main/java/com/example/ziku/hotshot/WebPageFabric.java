package com.example.ziku.hotshot;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ziku.hotshot.webpages.KomputronikWebPage;
import com.example.ziku.hotshot.webpages.MoreleWebPage;
import com.example.ziku.hotshot.webpages.ProlineWebPage;
import com.example.ziku.hotshot.webpages.SatysfakcjaWebPage;
import com.example.ziku.hotshot.webpages.WebPage;
import com.example.ziku.hotshot.webpages.XkomWebPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Ziku on 2016-07-11.
 */
public class WebPageFabric {

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

    public ContentValues returnNewWebSiteData(String webSite)
    {
        ContentValues cv;
        WebPage webPage;
        switch (webSite){
            case WebPageFabric.X_KOM:
                webPage = new XkomWebPage();
                break;
            case WebPageFabric.KOMPUTRONIK:
                webPage = new KomputronikWebPage();
                break;
            case WebPageFabric.MORELE:
                webPage = new MoreleWebPage();
                break;
            case WebPageFabric.PROLINE:
                webPage = new ProlineWebPage();
                break;
            case WebPageFabric.SATYSFAKCJA:
                webPage = new SatysfakcjaWebPage();
                break;
            default:
                webPage = new XkomWebPage();
                break;
        }
        cv = webPage.GetWebPageData();
        return cv;
    }
}
