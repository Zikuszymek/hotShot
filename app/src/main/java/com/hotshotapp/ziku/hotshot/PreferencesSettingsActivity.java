package com.hotshotapp.ziku.hotshot;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.hotshotapp.ziku.hotshot.management.SettingsActiveAdapter;
import com.hotshotapp.ziku.hotshot.tables.ActiveWebSites;

import java.util.List;

/**
 * Created by Ziku on 2017-01-29.
 */

public class PreferencesSettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
 //   public static SettingsActivity thisSettingsActivity;

    public static boolean ACTIVITY_ACTIVE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  thisSettingsActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_drawer_layout_global);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_settings_global);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_settings_global);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view_settings_gloabl);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        MainActivity.OnNavigationMenuChange(id,this);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_settings_global);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
        PreferencesSettingsActivity.ACTIVITY_ACTIVE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferencesSettingsActivity.ACTIVITY_ACTIVE = false;
    }
}
