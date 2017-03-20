package com.hotshotapp.ziku.hotshot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.hotshotapp.ziku.hotshot.management.HotShotRecyclerAdapter;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;
import com.hotshotapp.ziku.hotshot.management.SwipeViewAdapter;
import com.hotshotapp.ziku.hotshot.jsonservices.RESTRequestAndCallback;
import com.hotshotapp.ziku.hotshot.jsonservices.RetrofitRequestHotshot;
import com.hotshotapp.ziku.hotshot.services.HotShotAlarmReceiver;
import com.hotshotapp.ziku.hotshot.services.UniversalRefresh;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HotShotRecyclerAdapter.ActivityReactionOnAdapter {


    @BindView(R.id.all_button) ImageButton allButton;
    @BindView(R.id.electronic_button) ImageButton electronicButton;
    @BindView(R.id.others_button) ImageButton otherButton;
    @BindView(R.id.books_button) ImageButton bookButton;
    @BindView(R.id.clothes_button) ImageButton clothesButton;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private int whiteColor;
    private int orangeColor;
    private SwipeViewAdapter swipeViewAdapter;

    public static boolean ACTIVITY_ACTIVE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hotshot);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        whiteColor = Color.parseColor("#FFFFFF");
        orangeColor = Color.parseColor("#FF6600");

        swipeViewAdapter = new SwipeViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeViewAdapter);

        SetAllHSButtonActive();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SetAllButtonsNormal();
                switch (position) {
                    case 0:
                        SetAllHSButtonActive();
                        break;
                    case 1:
                        SetElectronicHSButtonActive();
                        break;
                    case 2:
                        SetOtherHSButtonActive();
                        break;
                    case 3:
                        SetBookHSButtonActive();
                        break;
                    case 4:
                        SetClothesButtonActive();
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
                viewPager.setCurrentItem(0, true);
                SetAllButtonsNormal();
                SetAllHSButtonActive();
            }
        });

        electronicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1, true);
                SetAllButtonsNormal();
                SetElectronicHSButtonActive();
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2, true);
                SetAllButtonsNormal();
                SetOtherHSButtonActive();
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3, true);
                SetAllButtonsNormal();
                SetBookHSButtonActive();
            }
        });

        clothesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(4, true);
                SetAllButtonsNormal();
                SetClothesButtonActive();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        OnNavigationMenuChange(id, this);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        UniversalRefresh.RefreshCategoriesAndWebPages(this);
        UniversalRefresh.AddNewGlobalInformationIfDoesNotExist(this);
        UniversalRefresh.RemoveAllNotifications(this);
        UniversalRefresh.RefreshAllIfNoLongedRefreshed(this);
//        if (SharedSettingsHS.GetPreferenceBoolen(getString(R.string.key_check_for_updates), getApplicationContext())) {
//            UniversalRefresh.CheckForNewAPKVersion(this);
//        }
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

    @Override
    protected void onDestroy() {
        Log.d("SERVIS", "Activity Destroyed");
        super.onDestroy();
    }

    private void SetHotShotsListView() {
        if (swipeViewAdapter != null) {
            swipeViewAdapter.refreshAllCreatedFragments();
        }
    }


    private void SetServiceAlarmManager() {
        HotShotAlarmReceiver.SetAlarmManager(this);
    }

    private void SetAllHSButtonActive() {
        allButton.setBackgroundColor(whiteColor);
        allButton.setImageResource(R.drawable.hotshot_button_orange);
    }

    private void SetElectronicHSButtonActive() {
        electronicButton.setBackgroundColor(whiteColor);
        electronicButton.setImageResource(R.drawable.electronic_button_orange);
    }

    private void SetOtherHSButtonActive() {
        otherButton.setBackgroundColor(whiteColor);
        otherButton.setImageResource(R.drawable.other_button_orange);
    }

    private void SetBookHSButtonActive() {
        bookButton.setImageResource(R.drawable.books_button_orange);
        bookButton.setBackgroundColor(whiteColor);
    }

    private void SetClothesButtonActive() {
        clothesButton.setImageResource(R.drawable.clothes_orange);
        clothesButton.setBackgroundColor(whiteColor);
    }

    private void SetAllButtonsNormal() {
        bookButton.setImageResource(R.drawable.books_button_white);
        bookButton.setBackgroundColor(orangeColor);
        otherButton.setBackgroundColor(orangeColor);
        otherButton.setImageResource(R.drawable.other_button_white);
        electronicButton.setBackgroundColor(orangeColor);
        electronicButton.setImageResource(R.drawable.electronic_button_white);
        allButton.setBackgroundColor(orangeColor);
        allButton.setImageResource(R.drawable.hotshot_button_white);
        clothesButton.setImageResource(R.drawable.clothes_white);
        clothesButton.setBackgroundColor(orangeColor);
    }

    public static void OnNavigationMenuChange(int id, Context context) {
        switch (id) {
            case R.id.okazje:
                if (!MainActivity.ACTIVITY_ACTIVE) {
                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(activityIntent);
                }
                if (SettingsActivity.thisSettingsActivity != null) {
                    SettingsActivity.thisSettingsActivity.finish();
                }

                if (Info.thisInfo != null) {
                    Info.thisInfo.finish();
                }
                break;
            case R.id.strony:
                if (!PreferencesSettingsActivity.ACTIVITY_ACTIVE) {
                    Intent openSettingsActivity = new Intent(context, PreferencesSettingsActivity.class);
                    openSettingsActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(openSettingsActivity);
                }
                if (Info.thisInfo != null) {
                    Info.thisInfo.finish();
                }
                break;
            case R.id.informacje:
                if (!Info.ACTIVITY_ACTIVE) {
                    Intent openInfoActivity = new Intent(context, Info.class);
                    openInfoActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(openInfoActivity);
                }
                if (SettingsActivity.thisSettingsActivity != null) {
                    SettingsActivity.thisSettingsActivity.finish();
                }
                break;
            case R.id.napisz:
                String mailto = "mailto:grezelek@gmail.com" +
                        "?cc=" + "" +
                        "&subject=" + Uri.encode("HotShot - uwagi, sądy, buziaki") +
                        "&body=" + Uri.encode("");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                context.startActivity(emailIntent);

                break;
            case R.id.ocen:
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToShopIntent = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    context.startActivity(goToShopIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }

                break;
        }
    }

    private void requestIgnoreBatteryOptymalization(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName))
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            else {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public void doActivityAction(ActiveHotShots activeHotShots, String fileName) {

        Intent intent = new Intent(this, ShowImageActivity.class);
        intent.putExtra(ShowImageActivity.DATA_FOR_DETAILS, activeHotShots);
        intent.putExtra(ShowImageActivity.IMG_URL, fileName);
        this.startActivity(intent);
    }
}
