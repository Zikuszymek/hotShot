package com.hotshotapp.ziku.hotshot.retrofit;

import com.hotshotapp.ziku.hotshot.jsonservices.KeyItems;
import com.hotshotapp.ziku.hotshot.tables.ActiveCategories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ziku on 2017-02-19.
 */

public interface HotShotRestAPI {

    @GET(KeyItems.CATEGORIES_REST)
    Call<List<ActiveCategories>> loadCategories();
}
