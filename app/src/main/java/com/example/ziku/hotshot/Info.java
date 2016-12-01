package com.example.ziku.hotshot;

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
import com.example.ziku.hotshot.management.InfoActiveAdapter;
import com.example.ziku.hotshot.management.SettingsActiveAdapter;
import com.example.ziku.hotshot.tables.ActiveInfo;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import java.util.Collections;
import java.util.List;

public class Info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView listView;
    private NavigationView navigationView;
    public static Info thisInfo;

    public static boolean ACTIVITY_ACTIVE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisInfo = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_drawer_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_info);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view_info);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.info_list);


        List<ActiveInfo> activeInfos = new Select().from(ActiveInfo.class).execute();
        Collections.sort(activeInfos);
        InfoActiveAdapter infoActiveAdapter = new InfoActiveAdapter(this, (activeInfos));
        listView.setAdapter(infoActiveAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        MainActivity.OnNavigationMenuChange(id,this);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_info);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
        Info.ACTIVITY_ACTIVE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Info.ACTIVITY_ACTIVE = false;
    }
}
