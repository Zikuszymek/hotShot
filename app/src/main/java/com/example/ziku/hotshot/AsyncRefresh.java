package com.example.ziku.hotshot;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ziku.hotshot.management.HotShotsAdapter;
import com.example.ziku.hotshot.tables.HotShotsTable;
import com.example.ziku.hotshot.tables.WebSiteTable;

/**
 * Created by Ziku on 2016-07-31.
 */
public class AsyncRefresh extends AsyncTask<Void,Void,Void>{

    private HotShotsDatabase db;
    private ImageButton refreshButton;
    private HotShotsAdapter hotShotsAdapter;
    private ListView listView;
    private Context context;
    private ConnectivityManager connectivityManager;

    public AsyncRefresh(HotShotsDatabase db,ImageButton refreshButton,HotShotsAdapter hotShotsAdapter, ListView listView, Context context, ConnectivityManager connectivityManager)
    {
        super();
        this.db = db;
        this.refreshButton = refreshButton;
        this.hotShotsAdapter = hotShotsAdapter;
        this.listView = listView;
        this.context = context;
        this.connectivityManager = connectivityManager;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        DoUpgradeOfProduct(WebPageFabric.KOMPUTRONIK);
        DoUpgradeOfProduct(WebPageFabric.X_KOM);
        DoUpgradeOfProduct(WebPageFabric.MORELE);
        DoUpgradeOfProduct(WebPageFabric.PROLINE);
        DoUpgradeOfProduct(WebPageFabric.SATYSFAKCJA);
//        String cos = db.GetProductName(WebPageFabric.X_KOM);
//        Log.d("ERROR",cos);
//        db.UpdateHotShot(WebPageFabric.X_KOM);

//        db.UpdateHotShot(WebPageFabric.MORELE);
//        db.UpdateHotShot(WebPageFabric.SATYSFAKCJA);
//        db.UpdateHotShot(WebPageFabric.PROLINE);
//        db.UpdateHotShot(WebPageFabric.KOMPUTRONIK);

//        hotShotsAdapter.changeCursor(db.GetAllHotShots());
//        hotShotsAdapter.notifyDataSetChanged();
        return null;
    }

       private void DoUpgradeOfProduct(String webPage)
    {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null) {
            if(networkInfo.isConnectedOrConnecting()) {
//                String before = db.GetProductName(webPage);
                db.UpdateHotShot(webPage);
//                String after = db.GetProductName(webPage);
                    Cursor cursor = db.GetImageAndName(webPage);
                    cursor.moveToFirst();
                    String imgName = cursor.getString(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NAME));
                    String imgUrl = cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.IMG_URL));
//                    GetImage getImage = new GetImage(imgName, imgUrl, context);
//                    getImage.execute();
                    Log.d("IMG", "image downloaded");

            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        hotShotsAdapter.changeCursor(db.GetAllHotShots());
        hotShotsAdapter.notifyDataSetChanged();
        refreshButton.setClickable(true);
        int color = Color.parseColor("#FF6600");
        refreshButton.setBackgroundColor(color);
        refreshButton.setImageResource(R.drawable.refresh);
       // refreshButton.setText("REFRESH");
        try {
            Thread.sleep(1000);
        }catch (Exception ex){}
        listView.setAdapter(hotShotsAdapter);

    }
}
