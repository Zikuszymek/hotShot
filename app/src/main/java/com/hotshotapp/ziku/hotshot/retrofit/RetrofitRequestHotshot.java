package com.hotshotapp.ziku.hotshot.retrofit;

import com.hotshotapp.ziku.hotshot.jsonservices.KeyItems;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Ziku on 2017-02-19.
 */

public class RetrofitRequestHotshot {

    private static Retrofit openRetrofit;

    public static Retrofit getRetrofitClietn(){
        if(openRetrofit == null){
            openRetrofit = CreateRetrofitClient();
        }
        return openRetrofit;
    }

    private static Retrofit CreateRetrofitClient() {

        OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KeyItems.REST_URL)
                .client(okhttpClient.build())
                .build();

        return retrofit;
    }
}
