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

/**
 * Created by Ziku on 2016-08-31.
 */
public class HotShotsFragment extends Fragment{
    private HotShotsDatabase database;
    private HotShotsAdapter hotShotsAdapter;
    private ListView listView;
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hots_shots_list,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.d("SWIPE","Fragment HotShot Created");
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.hot_shot_swipe_list);
        database = HotShotsDatabase.ReturnSingleInstance(getActivity());
        SetAdapterOnProperList();
    }

    public void SetAdapterOnProperList(){
        cursor = database.GetAllActiveHotShots();
        hotShotsAdapter = new HotShotsAdapter(getContext(), cursor, 0);
        listView.setAdapter(hotShotsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TEST","Clicked");

            }
        });
    }


}
