package com.hotshotapp.ziku.hotshot.jsonservices;

import com.hotshotapp.ziku.hotshot.jsonservices.KeyItems;
import com.hotshotapp.ziku.hotshot.tables.CategoriesGSONConverter;
import com.hotshotapp.ziku.hotshot.tables.HotShotsGSONConverter;
import com.hotshotapp.ziku.hotshot.tables.WebPagesGSONConverter;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Ziku on 2017-02-19.
 */

public class RetrofitRequestHotshot {

    private static HotShotRestAPI openRetrofit;

    public static HotShotRestAPI getRetrofitAPI(){
        if(openRetrofit == null){
            openRetrofit = CreateRetrofitClient();
        }
        return openRetrofit;
    }

    private static HotShotRestAPI CreateRetrofitClient() {

        OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KeyItems.REST_URL)
                .client(okhttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(HotShotRestAPI.class);
    }

    public interface HotShotRestAPI {

        @GET(KeyItems.CATEGORIES_REST)
        Call<List<CategoriesGSONConverter>> loadCategories();

        @GET(KeyItems.HOTSHOTS_REST)
        Call<List<HotShotsGSONConverter>> loadHotShots();

        @GET(KeyItems.WEBPAGES_REST)
        Call<List<WebPagesGSONConverter>> loadWebSites();
    }
}
