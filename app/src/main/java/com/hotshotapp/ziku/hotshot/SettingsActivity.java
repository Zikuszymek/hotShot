package com.hotshotapp.ziku.hotshot;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.hotshotapp.ziku.hotshot.management.SettingsActiveAdapter;
import com.hotshotapp.ziku.hotshot.tables.ActiveWebSites;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView listView;
    private NavigationView navigationView;
    public static SettingsActivity thisSettingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisSettingsActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_drawer_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_settings);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view_settings);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.settings_swipe_list);


        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).execute();
        SettingsActiveAdapter settingsAdapter = new SettingsActiveAdapter(this,activeWebSitesList);
        if(listView.getHeaderViewsCount()==0){
            LayoutInflater layoutInflater = getLayoutInflater();
            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.settings_header,listView,false);
            listView.addHeaderView(header,null,false);
        }
        listView.setAdapter(settingsAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        MainActivity.OnNavigationMenuChange(id,this);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_settings);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
