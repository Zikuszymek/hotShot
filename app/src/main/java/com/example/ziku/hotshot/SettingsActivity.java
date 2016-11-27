package com.example.ziku.hotshot;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.ziku.hotshot.management.SettingsActiveAdapter;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView) findViewById(R.id.settings_swipe_list);


        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).execute();
        SettingsActiveAdapter settingsAdapter = new SettingsActiveAdapter(this,activeWebSitesList);
        listView.setAdapter(settingsAdapter);
        if(listView.getHeaderViewsCount()==0){
            LayoutInflater layoutInflater = getLayoutInflater();
            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.settings_header,listView,false);
            listView.addHeaderView(header,null,false);
        }
    }

//    return inflater.inflate(R.layout.settings_list,container,false);
//}
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        ListView hotShotListView = (ListView) getActivity().findViewById(R.id.hot_shot_swipe_list);
//
//        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).execute();
//        SettingsActiveAdapter settingsAdapter = new SettingsActiveAdapter(getContext(),activeWebSitesList,hotShotListView);
//        listView.setAdapter(settingsAdapter);
//        if(listView.getHeaderViewsCount()==0){
//            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.settings_header,listView,false);
//            listView.addHeaderView(header,null,false);
//        }
}
