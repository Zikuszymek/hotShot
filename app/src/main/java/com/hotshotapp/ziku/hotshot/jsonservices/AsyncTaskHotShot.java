package com.hotshotapp.ziku.hotshot.jsonservices;

import android.content.Context;
import android.os.AsyncTask;

import com.hotshotapp.ziku.hotshot.services.UniversalRefresh;

/**
 * Created by Ziku on 2016-12-01.
 */

public class AsyncTaskHotShot extends AsyncTask<Void,Void,Void> {

    private Runnable executeAfter;
    private Context context;
    private boolean forced;
    private boolean errorOccured;

    public AsyncTaskHotShot(Runnable executeAfter, Context context, boolean forced) {
        this.executeAfter = executeAfter;
        this.context = context;
        this.forced = forced;
        this.errorOccured = false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            JsonHotShotsAsync jsonHotShotsAsync = new JsonHotShotsAsync(context, forced);
            jsonHotShotsAsync.ExecuteJsonRefreshing();
        } catch (Exception ex){
            errorOccured = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(executeAfter != null){
            executeAfter.run();
        }
        if(errorOccured){
            UniversalRefresh.AlerDialogWithMessage(UniversalRefresh.INTERNET_ERROR,context);
        }
    }
}
