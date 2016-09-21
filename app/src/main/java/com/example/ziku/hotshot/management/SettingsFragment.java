package com.example.ziku.hotshot.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveWebSites;

import java.util.List;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SettingsFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getActivity().findViewById(R.id.settings_swipe_list);
        ListView hotShotListView = (ListView) getActivity().findViewById(R.id.hot_shot_swipe_list);

        List<ActiveWebSites> activeWebSitesList = new Select().from(ActiveWebSites.class).execute();
        SettingsActiveAdapter settingsAdapter = new SettingsActiveAdapter(getContext(),activeWebSitesList,hotShotListView);
        listView.setAdapter(settingsAdapter);
        if(listView.getHeaderViewsCount()==0){
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.settings_header,listView,false);
            listView.addHeaderView(header,null,false);
        }
    }

}
