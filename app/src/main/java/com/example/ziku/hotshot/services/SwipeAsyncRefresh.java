package com.example.ziku.hotshot.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Ziku on 2016-11-27.
 */

public class SwipeAsyncRefresh extends AsyncTask<Void,Void,Void> {

    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SwipeAsyncRefresh(Activity activity, SwipeRefreshLayout swipeRefreshLayout){
        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        UniversalRefresh.GetAllJsonDataRefreshed(this.activity, this.swipeRefreshLayout);
        return null;
    }
}
