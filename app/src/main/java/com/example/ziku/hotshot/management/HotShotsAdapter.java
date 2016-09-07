package com.example.ziku.hotshot.management;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.HotShotsTable;
import com.example.ziku.hotshot.tables.WebSiteTable;

import java.io.File;

/**
 * Created by Ziku on 2016-08-06.
 */
public class HotShotsAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public HotShotsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.list_element,viewGroup,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final Cursor thatCursor = cursor;
        final LinearLayout content = (LinearLayout) view.findViewById(R.id.content_layout);
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView oldprice = (TextView) view.findViewById(R.id.old_price);
        TextView newPrie = (TextView) view.findViewById(R.id.new_price);
        TextView itemsLeft = (TextView) view.findViewById(R.id.items_left);
        TextView webSiteName = (TextView) view.findViewById(R.id.web_site_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);
        productName.setText(cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.PRODUCT_NAME)));
        oldprice.setText(cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.OLD_PRICE)));
        newPrie.setText(cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.NEW_PRICE)));
        itemsLeft.setText(cursor.getString(cursor.getColumnIndex(HotShotsTable.HotShotsColumn.ITEMS_LEFT)));
        webSiteName.setText(cursor.getString(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NAME)));
        final String urlString = cursor.getString(thatCursor.getColumnIndex(HotShotsTable.HotShotsColumn.PRODUCT_URL));

        final String fileName = cursor.getString(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NAME)) + ".png";
        File file = context.getFileStreamPath(fileName);
        if(file.exists()){
            Log.d("ERROR","file exists");
            Uri uri = Uri.fromFile(file);
            imageView.setImageURI(uri);
        }

        content.setFocusable(true);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                context.startActivity(openUrlIntent);
            }
        });

        imageView.setFocusable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String string = thatCursor.getString(thatCursor.getColumnIndex(HotShotsTable.HotShotsColumn.PRODUCT_NAME));
                Intent showImageIntent = new Intent(context, ShowImageActivity.class);
                showImageIntent.putExtra("ImageLocation",fileName);
                context.startActivity(showImageIntent);
            }
        });

    }

}
