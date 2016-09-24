package com.example.ziku.hotshot.management;

import android.os.AsyncTask;

import com.example.ziku.hotshot.tables.ActiveORMmanager;
import com.example.ziku.hotshot.webpages.HelionWebPage;
import com.example.ziku.hotshot.webpages.MallWebPage;
import com.example.ziku.hotshot.webpages.WebPage;
import com.example.ziku.hotshot.webpages.XkomWebPage;

/**
 * Created by Ziku on 2016-09-21.
 */
public class TestClass extends AsyncTask<Void,Void,Void>{
    @Override
    protected Void doInBackground(Void... voids) {
        WebPage webPage;
        webPage = new MallWebPage();
        webPage.GetWebPageData();
                return null;
    }
}
