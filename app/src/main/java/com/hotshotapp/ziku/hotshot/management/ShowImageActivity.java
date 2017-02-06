package com.hotshotapp.ziku.hotshot.management;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hotshotapp.ziku.hotshot.R;

import java.io.File;

/**
 * Created by Ziku on 2016-09-04.
 */
public class ShowImageActivity extends Activity {
    private ImageView imageView;
    private Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int)(displayMetrics.widthPixels * .8);
        int height = (int)(displayMetrics.heightPixels * 0.8);

        getWindow().setLayout(width,height);

        imageView = (ImageView) findViewById(R.id.show_image_view);
        thisIntent = getIntent();
        String fileName = thisIntent.getStringExtra("ImageLocation");

        File file = getFileStreamPath(fileName);
        if(file.exists()){
            Log.d("ERROR","file exists");
            Uri uri = Uri.fromFile(file);
            imageView.setImageURI(uri);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
