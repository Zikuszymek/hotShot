package com.example.ziku.hotshot.webpages;

import android.content.ContentValues;
import android.util.Log;

import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveORMmanager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ziku on 2016-07-30.
 */
public abstract class WebPage {

    protected final static String TAG= "WebPageCONNECTION";
    protected Document doc;
    protected ArrayList<ContentValues> cv;

    protected String productName = ActiveORMmanager.EMPTY;
    protected int oldPrice = ActiveORMmanager.NULL_PRICE;
    protected int newPrice = ActiveORMmanager.NULL_PRICE;
    protected String productUrl = ActiveORMmanager.EMPTY;
    protected String imgUrl = ActiveORMmanager.EMPTY;

    public WebPage() {}

    /*
    Constructor that create doc, dependend on provided url adress
     */
    public WebPage(String webUrl)
    {
        try{
            this.doc = Jsoup.connect(webUrl).get();
        }
        catch (IOException ex){
            Log.d(TAG,"Problem occured with connected to provided url: " + webUrl);
        }
    }

    /*
    Method returned ContentValues for provided data downloaded from specified web page
     */
    public ArrayList<ContentValues> ReturnCV(String productName, int odlPrice, int newPrice, String imgUrl, String productUrl)
    {
        cv = new ArrayList<>();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActiveHotShots.PRODUCT_NAME,productName);
        contentValues.put(ActiveHotShots.OLD_PRICE,odlPrice);
        contentValues.put(ActiveHotShots.NEW_PRICE,newPrice);
        contentValues.put(ActiveHotShots.IMG_URL,imgUrl);
        contentValues.put(ActiveHotShots.PRODUCT_URL,productUrl);

        cv.add(contentValues);
        return cv;
    }

    /*
    Individual method for each class that will download specified data from web page
     */
    public abstract ArrayList<ContentValues> GetWebPageData();

}
