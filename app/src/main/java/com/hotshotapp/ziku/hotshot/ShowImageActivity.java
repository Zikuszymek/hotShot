package com.hotshotapp.ziku.hotshot;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ziku on 2016-09-04.
 */
public class ShowImageActivity extends AppCompatActivity {

    public static final String DATA_FOR_DETAILS = "data_for_details";
    public static final String IMG_URL = "img_url";
    public static final String TRANSITION_ID = "transition_id";

    @BindView(R.id.show_image_view)
    ImageView imageView;

    @BindView(R.id.product_name)
    TextView productName;

    @BindView(R.id.old_price)
    TextView oldPrice;

    @BindView(R.id.new_price)
    TextView newPrice;

    @BindView(R.id.percentage_difference)
    TextView percentageDifference;

    @BindView(R.id.vendor_name)
    TextView textView;

    @BindView(R.id.go_to_web_site_button)
    LinearLayout goToWebSite;

    @BindView(R.id.view_1)
    View view1;

    @BindView(R.id.view_2)
    View view2;

    @BindView(R.id.layout_1)
    LinearLayout linearLayout1;

    @BindView(R.id.layout_2)
    LinearLayout linearLayout2;

    @BindView(R.id.frame_show_image_layout)
    FrameLayout frameLayout;

    @BindView(R.id.scroll_view_image)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.show_image);
        ButterKnife.bind(this);

        final ActiveHotShots activeHotShots = getIntent().getParcelableExtra(DATA_FOR_DETAILS);

        String test = bundle.getString(TRANSITION_ID);
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setTransitionName(test);
        }
        String fileName = bundle.getString(IMG_URL);

        final File file = getFileStreamPath(fileName);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Glide.with(this).load(uri).into(imageView);
        }

        productName.setText(activeHotShots.productName);
        newPrice.setText(String.valueOf(activeHotShots.newPrice) + " zł");
        textView.setText(activeHotShots.webSites.webSiteName);

        if (activeHotShots.oldPrice != 0) {
            String oldPriceString = String.valueOf(activeHotShots.oldPrice) + " zł";
            if (activeHotShots.oldPrice != 0 && activeHotShots.newPrice != 0) {
                int percentage = ((activeHotShots.oldPrice - activeHotShots.newPrice) * 100) / activeHotShots.oldPrice;
                String percentageString = "-" + String.valueOf(percentage) + "%";
                percentageDifference.setText(percentageString);
            } else {
                percentageDifference.setText("");
                percentageDifference.setVisibility(View.GONE);
            }
            oldPrice.setText(oldPriceString);
        } else {
            percentageDifference.setVisibility(View.GONE);
            oldPrice.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
        }

        goToWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activeHotShots.productUrl));
                Intent chooser = Intent.createChooser(openUrlIntent, "Wybierz przeglądarkę");
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooser);
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
