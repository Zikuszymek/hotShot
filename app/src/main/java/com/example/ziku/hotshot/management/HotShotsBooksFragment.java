package com.example.ziku.hotshot.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

/**
 * Created by Ziku on 2016-11-19.
 */

public class HotShotsBooksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.books_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d("SWIPE","Fragment HotShot Created");
        super.onActivityCreated(savedInstanceState);
        ListView listView = (ListView) getActivity().findViewById(R.id.books_swipe_list);
        List<ActiveHotShots> activeWebSitesList =  ActiveHotShots.ReturnAllActiveHotShotsActive(2);
        HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(getContext(), activeWebSitesList);
        listView.setAdapter(null);
        listView.setAdapter(hotShotsAdapter);
    }
}
