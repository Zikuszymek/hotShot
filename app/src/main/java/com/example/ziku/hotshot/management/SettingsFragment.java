package com.example.ziku.hotshot.management;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ziku.hotshot.HotShotsDatabase;
import com.example.ziku.hotshot.R;

import java.util.List;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SettingsFragment extends Fragment{
    private HotShotsDatabase database;
    private SettingsAdapter settingsAdapter;
    private ListView listView;
    private ListView hotShotListView;
    private Cursor cursor;
    private ViewGroup header;
    private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_list,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView = (ListView) getActivity().findViewById(R.id.settings_swipe_list);
        hotShotListView = (ListView) getActivity().findViewById(R.id.hot_shot_swipe_list);

        database = HotShotsDatabase.ReturnSingleInstance(getActivity());
        cursor = database.GetAllSettings();
        settingsAdapter = new SettingsAdapter(getContext(),cursor,0,hotShotListView);
        listView.setAdapter(settingsAdapter);
        if(listView.getHeaderViewsCount()==0){
            layoutInflater = getActivity().getLayoutInflater();
            header = (ViewGroup) layoutInflater.inflate(R.layout.settings_header,listView,false);
            listView.addHeaderView(header,null,false);
        }
    }

}
