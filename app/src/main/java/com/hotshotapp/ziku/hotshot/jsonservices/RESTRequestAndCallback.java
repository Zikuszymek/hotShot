package com.hotshotapp.ziku.hotshot.jsonservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;
import com.hotshotapp.ziku.hotshot.tables.CategoriesGSONConverter;
import com.hotshotapp.ziku.hotshot.tables.HotShotsGSONConverter;
import com.hotshotapp.ziku.hotshot.tables.WebPagesGSONConverter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

import static com.hotshotapp.ziku.hotshot.services.UniversalRefresh.AlerDialogWithMessage;
import static com.hotshotapp.ziku.hotshot.services.UniversalRefresh.INTERNET_ERROR;

public class RESTRequestAndCallback {

    private RetrofitRequestHotshot.HotShotRestAPI hotShotRestAPI;
    private Context context;

    public RESTRequestAndCallback(RetrofitRequestHotshot.HotShotRestAPI hotShotRestAPI, Context context){
        this.hotShotRestAPI = hotShotRestAPI;
        this.context = context;
    }

    public void getAllCategories(){
        Call<List<CategoriesGSONConverter>> call = hotShotRestAPI.loadCategories();
        try {
            List<CategoriesGSONConverter> categoriesGSONConverterList = call.execute().body();
            for (CategoriesGSONConverter activeCategories: categoriesGSONConverterList){
                activeCategories.convertToActiveCategory();
            }
        } catch (IOException e) {
            AlerDialogWithMessage(INTERNET_ERROR,context);
        }
    }

    public void getAllWebPages(){
        Call<List<WebPagesGSONConverter>> call = hotShotRestAPI.loadWebSites();
        try {
            List<WebPagesGSONConverter> webPagesGSONConverterList = call.execute().body();
            for(WebPagesGSONConverter webPagesGSONConverter : webPagesGSONConverterList){
                webPagesGSONConverter.convertToActiveWebPage();
            }
        } catch (IOException e) {
            AlerDialogWithMessage(INTERNET_ERROR,context);
        }
    }

    public void getAllHotShots(final boolean forced){
        Call<List<HotShotsGSONConverter>> call = hotShotRestAPI.loadHotShots();
        try {
            List<HotShotsGSONConverter> hotShotsGSONConverterList = call.execute().body();
            for(HotShotsGSONConverter activeHotShots : hotShotsGSONConverterList){
                Notification notification = activeHotShots.convertToActiveHotShot(context,forced);

                if(notification != null && !forced && SharedSettingsHS.GetPreferenceBoolen(context.getString(R.string.key_notyfication),context)){
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notification);
                }
            }
        } catch (IOException e) {
            AlerDialogWithMessage(INTERNET_ERROR,context);
        }
    }

}
