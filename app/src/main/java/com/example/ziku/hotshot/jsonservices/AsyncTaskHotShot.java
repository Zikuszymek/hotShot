package com.example.ziku.hotshot.jsonservices;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Ziku on 2016-12-01.
 */

public class AsyncTaskHotShot extends AsyncTask<Void,Void,Void> {

    private Runnable executeAfter;
    private Context context;
    private boolean forced;

    public AsyncTaskHotShot(Runnable executeAfter, Context context, boolean forced) {
        this.executeAfter = executeAfter;
        this.context = context;
        this.forced = forced;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JsonHotShotsAsync jsonHotShotsAsync = new JsonHotShotsAsync(context,forced);
        jsonHotShotsAsync.ExecuteJsonRefreshing();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(executeAfter != null){
            executeAfter.run();
        }
    }
}
