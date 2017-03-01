package com.hotshotapp.ziku.hotshot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UpdateToNewVersionActivity extends AppCompatActivity {

    Context context;

    @BindView(R.id.update_button)
    Button declineButton;

    @BindView(R.id.not_now_button)
    Button acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_to_new_version);
        context = this;

        ButterKnife.bind(this);

        acceptButton = (Button) findViewById(R.id.update_button);
        declineButton = (Button) findViewById(R.id.not_now_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToShopIntent = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    context.startActivity(goToShopIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
                finish();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
