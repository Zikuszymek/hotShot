package com.example.ziku.hotshot.management;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.io.File;
import java.util.List;

/**
 * Created by Ziku on 2016-08-06.
 */
public class HotShotsActiveAdapter extends ArrayAdapter<ActiveHotShots> {

    public HotShotsActiveAdapter(Context context, List<ActiveHotShots> activeHotShotsList) {
        super(context, 0,activeHotShotsList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ActiveHotShots activeHotShots = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element,parent,false);
        }
        final LinearLayout content = (LinearLayout) convertView.findViewById(R.id.content_layout);
        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        TextView oldprice = (TextView) convertView.findViewById(R.id.old_price);
        TextView newPrie = (TextView) convertView.findViewById(R.id.new_price);
        TextView percentageDifference = (TextView) convertView.findViewById(R.id.percentage_difference);
        TextView webSiteName = (TextView) convertView.findViewById(R.id.web_site_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.product_image);
//        Log.d("ACTIVE",activeHotShots.productName + " " + activeHotShots.webSites.webSiteName);

        productName.setText(activeHotShots.productName);
        String oldPriceString = String.valueOf(activeHotShots.oldPrice) + " zł";
        String newPriceString = String.valueOf(activeHotShots.newPrice) + " zł";
        if(activeHotShots.oldPrice!=0 && activeHotShots.newPrice != 0) {
            int percentage = ((activeHotShots.oldPrice - activeHotShots.newPrice) * 100)/activeHotShots.oldPrice;
            String percentageString = "-" + String.valueOf(percentage) + "%";
            percentageDifference.setText(percentageString);
        } else {percentageDifference.setText("");}

        oldprice.setText(oldPriceString);
        newPrie.setText(newPriceString);
        webSiteName.setText(activeHotShots.webSites.webSiteName);


        final String urlString = activeHotShots.productUrl;

        final String fileName = activeHotShots.webSites.webSiteName + ".png";
        File file = getContext().getFileStreamPath(fileName);
        if(file.exists()){
            if(file.length() > 0){
                Uri uri = Uri.fromFile(file);
                imageView.setImageURI(uri);
            } else {
                imageView.setImageResource(R.drawable.hot_shot_icon);
            }
        }

        content.setFocusable(true);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                getContext().startActivity(openUrlIntent);
            }
        });

        imageView.setFocusable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showImageIntent = new Intent(getContext(), ShowImageActivity.class);
                showImageIntent.putExtra("ImageLocation",fileName);
                getContext().startActivity(showImageIntent);
            }
        });

        return convertView;
    }

}
