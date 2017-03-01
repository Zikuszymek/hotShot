package com.hotshotapp.ziku.hotshot.services;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.MainActivity;
import com.hotshotapp.ziku.hotshot.PreferencesSettingsActivity;
import com.hotshotapp.ziku.hotshot.UpdateToNewVersionActivity;

import org.jsoup.Jsoup;

/**
 * Created by Ziku on 2016-12-27.
 */

public class CheckForUpdates extends AsyncTask<Void,Void,Void>{

    private Context context;
    private Runnable after;

    public CheckForUpdates(Context context, Runnable after){
        this.context = context;
        this.after = after;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String newVersion = null;
        try{
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=pl")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (Exception ex){
            newVersion = null;
        }

        String currentVersion;
        try{
            currentVersion=  context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException ex){
            currentVersion = null;
        }

        if(UpdateIsNeededCheckVersions(currentVersion,newVersion)){
            Intent intent = new Intent(context, UpdateToNewVersionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(after != null){
            after.run();
        }
    }

    private boolean UpdateIsNeededCheckVersions(String currentVersion, String versionFromGooglePlay){
        try{
            double currentV = Double.valueOf(currentVersion);
            double googleV = Double.valueOf(versionFromGooglePlay);

            if(googleV > currentV){
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException ex){
            return true;
        }
    }
}
