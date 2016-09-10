package com.example.ziku.hotshot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.ziku.hotshot.management.HotShotsAdapter;
import com.example.ziku.hotshot.management.SettingsAdapter;
import com.example.ziku.hotshot.management.SwipeViewAdapter;
import com.example.ziku.hotshot.services.HotShotAsyncTast;
import com.example.ziku.hotshot.services.HotShotAlarmReceiver;

public class MainActivity extends FragmentActivity {

    private ListView hotShotListView;
    private HotShotsAdapter hotShotsAdapter;
    private SettingsAdapter settingsAdapter;
    private HotShotsDatabase database;
    private ImageButton refreshButton;
    private ImageButton hotShotButton;
    private ImageButton settingButton;
    private Cursor mainCursor;
    private Cursor settingCursor;
    private ConnectivityManager connectivityManager;
    private Context context;
    private int whiteColor;
    private int orangeColor;
    private LayoutInflater layoutInflater;
    private ViewGroup header;
    private SwipeViewAdapter swipeViewAdapter;
    private ViewPager viewPager;
    private boolean hotShotRefreshSeted;

    public static boolean APP_IS_RUNNING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TEST","On Create");
        database = HotShotsDatabase.ReturnSingleInstance(this);
        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        hotShotButton = (ImageButton) findViewById(R.id.hot_shots_button);
        whiteColor = Color.parseColor("#FFFFFF");
        orangeColor = Color.parseColor("#FF6600");
        context = this;
        layoutInflater = getLayoutInflater();
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        swipeViewAdapter = new SwipeViewAdapter(getSupportFragmentManager(),database);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(swipeViewAdapter);

        SetHotShotButtonActive();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        SetHotShotButtonActive();
                        SetSettingsButtonNormal();
                        Log.d("SWIPE","SWIPE 1");
                        break;
                    case 1:
                        SetSettingsButtonActive();
                        SetHotShotButtonNormal();
                        Log.d("SWIPE","SWIPE 2");
                        if(!hotShotRefreshSeted) {
                            hotShotRefreshSeted = true;
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        hotShotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0,true);
                SetHotShotButtonActive();
                SetSettingsButtonNormal();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                refreshButton.setClickable(false);
                refreshButton.setBackgroundColor(whiteColor);
                refreshButton.setImageResource(R.drawable.orange_refresh);
                refreshButton.startAnimation(AnimationUtils.loadAnimation(context,R.anim.rotate));
                Toast refreshToast = Toast.makeText(context,R.string.refreshing,Toast.LENGTH_SHORT);
                refreshToast.show();
                HotShotAsyncTast asyncRefresh = new HotShotAsyncTast(database, getApplicationContext(), connectivityManager, new Runnable() {
                    @Override
                    public void run() {

                        ListView listView = (ListView) findViewById(R.id.hot_shot_swipe_list);
                        Cursor cursor = database.GetAllActiveHotShots();
                        hotShotsAdapter = new HotShotsAdapter(context, cursor, 0);
                        listView.setAdapter(hotShotsAdapter);
                        refreshButton.setClickable(true);
                        refreshButton.setBackgroundColor(orangeColor);
                        refreshButton.setImageResource(R.drawable.refresh);
                        refreshButton.clearAnimation();
                    }
                }, true);
                asyncRefresh.execute(WebPageFabric.KOMPUTRONIK,WebPageFabric.X_KOM,
                        WebPageFabric.MORELE,WebPageFabric.PROLINE,WebPageFabric.SATYSFAKCJA);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1,true);
                SetSettingsButtonActive();
                SetHotShotButtonNormal();
           }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TEST","onResume");
        if(APP_IS_RUNNING)
            SetHotShotsListView();
        SetServiceAlarmManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.APP_IS_RUNNING = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void SetHotShotsListView() {
        ListView hotShotListView = (ListView) this.findViewById(R.id.hot_shot_swipe_list);
        HotShotsDatabase database = HotShotsDatabase.ReturnSingleInstance(this);
        Cursor thisCursor = database.GetAllActiveHotShots();
        HotShotsAdapter hotShotsAdapter = new HotShotsAdapter(this, thisCursor, 0);
        hotShotListView.setAdapter(hotShotsAdapter);
    }


    private void SetServiceAlarmManager() {
        Log.d("TEST", "check if service can be started");

            int TIMER = 60 * 60 * 1000;
            long PERIOD = 1 * 60 * 1000;

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent serviceIntent = new Intent(getBaseContext(), HotShotAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD, TIMER, pendingIntent);
            Log.d("TEST", "start service");
//        }
    }

    private void SetSettingsButtonActive(){
        settingButton.setBackgroundColor(whiteColor);
        settingButton.setImageResource(R.drawable.settings_orange);
    }

    private void SetHotShotButtonActive(){
        hotShotButton.setBackgroundColor(whiteColor);
        hotShotButton.setImageResource(R.drawable.hot_shot_orange);
    }

    private void SetSettingsButtonNormal(){
        settingButton.setBackgroundColor(orangeColor);
        settingButton.setImageResource(R.drawable.settings);
    }

    private void SetHotShotButtonNormal(){
        hotShotButton.setBackgroundColor(orangeColor);
        hotShotButton.setImageResource(R.drawable.hot_shot_white);
    }
}
