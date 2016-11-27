package com.example.ziku.hotshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;


import com.example.ziku.hotshot.management.HotShotsActiveAdapter;
import com.example.ziku.hotshot.management.SwipeViewAdapter;
import com.example.ziku.hotshot.services.HotShotAlarmReceiver;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageButton allButton;
    private ImageButton electronicButton;
    private ImageButton otherButton;
    private ImageButton bookButton;
    private int whiteColor;
    private int orangeColor;
    private SwipeViewAdapter swipeViewAdapter;
    private ViewPager viewPager;

    public static boolean APP_IS_RUNNING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hotshot);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d("TEST","On Create");

        allButton = (ImageButton) findViewById(R.id.all_button);
        electronicButton = (ImageButton) findViewById(R.id.electronic_button);
        otherButton = (ImageButton) findViewById(R.id.others_button);
        bookButton = (ImageButton) findViewById(R.id.books_button);
        whiteColor = Color.parseColor("#FFFFFF");
        orangeColor = Color.parseColor("#FF6600");

        swipeViewAdapter = new SwipeViewAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(swipeViewAdapter);

        SetAllHSButtonActive();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SetAllButtonsNormal();
                switch (position){
                    case 0:
                        SetAllHSButtonActive();
                        Log.d("SWIPE","SWIPE 1");
                        break;
                    case 1:
                        SetElectronicHSButtonActive();
                        Log.d("SWIPE","SWIPE 2");
                        break;
                    case 2:
                        SetOtherHSButtonActive();
                        Log.d("SWIPE","SWIPE 3");
                        break;
                    case 3:
                        SetBookHSButtonActive();
                        Log.d("SWIPE","SWIPE 4");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

      /*  swipeRefreshAll = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_all);
        swipeRefreshElectronic = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_electronic);
        swipeRefreshOther = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_others);
        swipeRefreshBook = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_books);

        swipeRefreshAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefrehsAllListsData();
            }
        });
        */

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0,true);
                SetAllButtonsNormal();
                SetAllHSButtonActive();
            }
        });

        electronicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1,true);
                SetAllButtonsNormal();
                SetElectronicHSButtonActive();
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2,true);
                SetAllButtonsNormal();
                SetOtherHSButtonActive();
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3,true);
                SetAllButtonsNormal();
                SetBookHSButtonActive();
            }
        });

//                JsonCategoriesAsync jsonCategoriesAsync = new JsonCategoriesAsync();
//                try {
//                    Void cos = jsonCategoriesAsync.execute().get();
//                } catch (Exception ex) {
//
//                }
//
//                JsonWebPagesAsync webPagesAsync = new JsonWebPagesAsync();
//                try {
//                   Void cos = webPagesAsync.execute().get();
//                } catch (Exception ex) {
//
//                }
//
//                JsonHotShotsAsync hotShotsAsync = new JsonHotShotsAsync();
//                hotShotsAsync.execute();


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.okazje:
                break;
            case R.id.strony:
                Intent openSettingsActivity = new Intent(this,SettingsActivity.class);
                startActivity(openSettingsActivity);
                break;
            case R.id.informacje:
                break;
            case R.id.napisz:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,"zikuszymek@o2.pl");
                intent.putExtra(Intent.EXTRA_SUBJECT, "test");
                intent.putExtra(Intent.EXTRA_TEXT, "ytedg");

                startActivity(Intent.createChooser(intent,""));
                break;
        }
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
            List<ActiveHotShots> activeWebSitesList = ActiveHotShots.ReturnAllActiveHotShotsActive(0);
            HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(this, activeWebSitesList);
            listView.setAdapter(null);
            listView.setAdapter(hotShotsAdapter);
        }
    }


    private void SetServiceAlarmManager() {
        HotShotAlarmReceiver alarmReceiver = new HotShotAlarmReceiver();
        alarmReceiver.SetAlarmManager(this);
    }

    private void SetAllHSButtonActive(){
        allButton.setBackgroundColor(whiteColor);
        allButton.setImageResource(R.drawable.hotshot_button_orange);
    }

    private void SetElectronicHSButtonActive(){
        electronicButton.setBackgroundColor(whiteColor);
        electronicButton.setImageResource(R.drawable.electronic_button_orange);
    }

    private void SetOtherHSButtonActive(){
        otherButton.setBackgroundColor(whiteColor);
        otherButton.setImageResource(R.drawable.other_button_orange);
    }

    private void SetBookHSButtonActive(){
        bookButton.setImageResource(R.drawable.books_button_orange);
        bookButton.setBackgroundColor(whiteColor);
    }

    private void SetAllButtonsNormal(){
        bookButton.setImageResource(R.drawable.books_button_white);
        bookButton.setBackgroundColor(orangeColor);
        otherButton.setBackgroundColor(orangeColor);
        otherButton.setImageResource(R.drawable.other_button_white);
        electronicButton.setBackgroundColor(orangeColor);
        electronicButton.setImageResource(R.drawable.electronic_button_white);
        allButton.setBackgroundColor(orangeColor);
        allButton.setImageResource(R.drawable.hotshot_button_white);
    }

    private void RefrehsAllListsData(){
        int i = 1;
    }
}
