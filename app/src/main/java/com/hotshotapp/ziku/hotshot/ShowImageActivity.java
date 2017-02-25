package com.hotshotapp.ziku.hotshot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import java.io.File;

/**
 * Created by Ziku on 2016-09-04.
 */
public class ShowImageActivity extends Activity {

    public static final String DATA_FOR_DETAILS = "data_for_details";
    public static final String IMG_URL = "img_url";
    private ImageView imageView;
    private Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);
        ActiveHotShots activeHotShots = getIntent().getParcelableExtra(DATA_FOR_DETAILS);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        int width = (int)(displayMetrics.widthPixels * .8);
//        int height = (int)(displayMetrics.heightPixels * 0.8);
//
//        getWindow().setLayout(width,height);

        imageView = (ImageView) findViewById(R.id.show_image_view);
        thisIntent = getIntent();
        String fileName = thisIntent.getStringExtra(IMG_URL);

        File file = getFileStreamPath(fileName);
        if (file.exists()) {
            Log.d("ERROR", "file exists");
            Uri uri = Uri.fromFile(file);
            imageView.setImageURI(uri);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getEnterTransition().setDuration(500);
        }
    }
}
