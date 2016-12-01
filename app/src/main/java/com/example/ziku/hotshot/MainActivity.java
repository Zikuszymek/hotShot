package com.example.ziku.hotshot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.ziku.hotshot.services.UniversalRefresh;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveInfo;

import java.util.Date;
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
    private NavigationView navigationView;

    public static boolean ACTIVITY_ACTIVE = false;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        OnNavigationMenuChange(id,this);
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
        UniversalRefresh.RefreshCategoriesAndWebPages(this);
        UniversalRefresh.AddNewGlobalInformationIfDoesNotExist(this);
        SetServiceAlarmManager();
        navigationView.getMenu().getItem(0).setChecked(true);
        MainActivity.ACTIVITY_ACTIVE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.ACTIVITY_ACTIVE = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetHotShotsListView();
    }

    private void SetHotShotsListView() {
        UniversalRefresh.RefreshAllIfPossible(this);
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

    public static void OnNavigationMenuChange(int id, Context context){
        switch (id){
            case R.id.okazje:
                if(!MainActivity.ACTIVITY_ACTIVE){
                    Intent activityIntent = new Intent(context,MainActivity.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(activityIntent);
                }
                if(SettingsActivity.thisSettingsActivity!=null){
                    SettingsActivity.thisSettingsActivity.finish();
                }

                if(Info.thisInfo!=null){
                    Info.thisInfo.finish();
                }
                break;
            case R.id.strony:
                if(!SettingsActivity.ACTIVITY_ACTIVE) {
                    Intent openSettingsActivity = new Intent(context, SettingsActivity.class);
                    openSettingsActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(openSettingsActivity);
                }
                if(Info.thisInfo!=null){
                    Info.thisInfo.finish();
                }
                break;
            case R.id.informacje:
                if(!Info.ACTIVITY_ACTIVE){
                    Intent openInfoActivity = new Intent(context,Info.class);
                    openInfoActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(openInfoActivity);
                }
                if(SettingsActivity.thisSettingsActivity!=null){
                    SettingsActivity.thisSettingsActivity.finish();
                }
                break;
            case R.id.napisz:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"grezelek@gmail.pl"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "HotShot - uwagi, sądy, buziaki");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setType("message/rfc822");

                context.startActivity(Intent.createChooser(intent,""));
                break;
        }
    }

}
