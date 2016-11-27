package com.example.ziku.hotshot.management;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.services.SwipeAsyncRefresh;
import com.example.ziku.hotshot.services.UniversalRefresh;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

/**
 * Created by Ziku on 2016-11-19.
 */

public class HotShotElectronicsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.electronics_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d("SWIPE","Fragment HotShot Created");
        super.onActivityCreated(savedInstanceState);
//        ListView listView = (ListView) getActivity().findViewById(R.id.electronics_swipe_list);
//        List<ActiveHotShots> activeWebSitesList =  ActiveHotShots.ReturnAllActiveHotShotsActive(1);
//        HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(getContext(), activeWebSitesList);
//        listView.setAdapter(null);
//        listView.setAdapter(hotShotsAdapter);
        UniversalRefresh.RefreshElectronicList(getActivity());

        final SwipeRefreshLayout swipeRefreshElectronic = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_electronic);
        swipeRefreshElectronic.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshElectronic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeAsyncRefresh swipeAsyncRefresh = new SwipeAsyncRefresh(getActivity(),swipeRefreshElectronic);
                swipeAsyncRefresh.execute();
            }
        });
    }
}
