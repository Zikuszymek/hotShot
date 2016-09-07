package com.example.ziku.hotshot.webpages;

import android.content.ContentValues;
import android.util.Log;

import com.example.ziku.hotshot.tables.HotShotsTable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ziku on 2016-07-30.
 */
public abstract class WebPage {

    protected final static String TAG= "WebPageCONNECTION";
    protected Document doc;
    protected ContentValues cv;

    protected String productName = "-";
    protected String oldPrice = "-";
    protected String newPrice = "-";
    protected String itemsLeft = "-";
    protected String productUrl = "-";
    protected String imgUrl = "-";

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
    public ContentValues ReturnCV(String productName, String odlPrice, String newPrice, String itemLeft, String imgUrl, String productUrl)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        String date = dateFormat.format(now);

        this.cv = new ContentValues();
        this.cv.put(HotShotsTable.HotShotsColumn.PRODUCT_NAME,productName);
        this.cv.put(HotShotsTable.HotShotsColumn.OLD_PRICE,odlPrice);
        this.cv.put(HotShotsTable.HotShotsColumn.NEW_PRICE,newPrice);
        this.cv.put(HotShotsTable.HotShotsColumn.ITEMS_LEFT,itemLeft);
        this.cv.put(HotShotsTable.HotShotsColumn.LAST_CHECK,date);
        this.cv.put(HotShotsTable.HotShotsColumn.IMG_URL,imgUrl);
        this.cv.put(HotShotsTable.HotShotsColumn.PRODUCT_URL,productUrl);
        Log.d("TIME",date);

        return cv;
    }

    /*
    Individual method for each class that will download specified data from web page
     */
    public abstract ContentValues GetWebPageData();

}
