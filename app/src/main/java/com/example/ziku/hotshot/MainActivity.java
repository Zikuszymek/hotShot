package com.example.ziku.hotshot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
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


import com.example.ziku.hotshot.management.HotShotsActiveAdapter;
import com.example.ziku.hotshot.management.SettingsActiveAdapter;
import com.example.ziku.hotshot.management.SwipeViewAdapter;
import com.example.ziku.hotshot.management.TestClass;
import com.example.ziku.hotshot.services.ActiveAsyncRefresh;
import com.example.ziku.hotshot.services.HotShotAlarmReceiver;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveORMmanager;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ListView hotShotListView;
    private HotShotsActiveAdapter hotShotsAdapter;
    private SettingsActiveAdapter settingsAdapter;
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
    private SharedPreferences sharedPreferences;

    private static final String DATABASE_WAS_CREATED = "DATABASE_WAS_CREATED";

    public static boolean APP_IS_RUNNING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TEST","On Create");

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.getBoolean(DATABASE_WAS_CREATED,false)) {
            Log.d("ACTIVE","Adding elements to database");
            ActiveORMmanager.AddWebsitesIfNotExists();
            ActiveORMmanager.AddStaticHotShots();
            editor.putBoolean(DATABASE_WAS_CREATED,true);
            editor.commit();
        }

        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        hotShotButton = (ImageButton) findViewById(R.id.hot_shots_button);
        whiteColor = Color.parseColor("#FFFFFF");
        orangeColor = Color.parseColor("#FF6600");
        context = this;
        layoutInflater = getLayoutInflater();
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        swipeViewAdapter = new SwipeViewAdapter(getSupportFragmentManager());
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
                ActiveAsyncRefresh activeAsyncRefresh = new ActiveAsyncRefresh( getApplicationContext(), new Runnable() {
                    @Override
                    public void run() {

                        ListView listView = (ListView) findViewById(R.id.hot_shot_swipe_list);
                        hotShotsAdapter = new HotShotsActiveAdapter(context, ActiveHotShots.ReturnAllActiveHotShotsActive());
                        listView.setAdapter(hotShotsAdapter);
                        refreshButton.setClickable(true);
                        refreshButton.setBackgroundColor(orangeColor);
                        refreshButton.setImageResource(R.drawable.refresh);
                        refreshButton.clearAnimation();
                    }
                }, true);
                activeAsyncRefresh.execute(ActiveORMmanager.X_KOM, ActiveORMmanager.KOMPUTRONIK, ActiveORMmanager.SATYSFAKCJA,
                        ActiveORMmanager.MORELE, ActiveORMmanager.PROLINE, ActiveORMmanager.HELION);
//                activeAsyncRefresh.execute(ActiveORMmanager.MALL);
//                TestClass testClass = new TestClass();
//                testClass.execute();
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
        SetServiceAlarmManager();
        SetHotShotsListView();
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
        ListView listView = (ListView) findViewById(R.id.hot_shot_swipe_list);
        if (listView != null) {
            List<ActiveHotShots> activeWebSitesList = ActiveHotShots.ReturnAllActiveHotShotsActive();
            HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(this, activeWebSitesList);
            listView.setAdapter(null);
            listView.setAdapter(hotShotsAdapter);
        }
    }


    private void SetServiceAlarmManager() {
        HotShotAlarmReceiver alarmReceiver = new HotShotAlarmReceiver();
        alarmReceiver.SetAlarmManager(this);
//        Log.d("TEST", "check if service can be started");

//            long TIMER = 60 * 60 * 1000L;
//            long PERIOD = 2 * 60 * 1000L;
//            long PROPER_START_TIME = SystemClock.elapsedRealtime() + PERIOD;
//            long PROPER_START_TIME = (System.currentTimeMillis()-(Calendar.getInstance().get(Calendar.MINUTE)*60*1000)) + (PERIOD) + TIMER;


//            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Intent serviceIntent = new Intent(MainActivity.this, HotShotAlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1113, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            manager.cancel(pendingIntent);
//            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, PERIOD, AlarmManager.INTERVAL_HOUR, pendingIntent);
//            Log.d("TEST", "start service" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(PROPER_START_TIME)));
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
