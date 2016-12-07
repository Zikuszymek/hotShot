package com.example.ziku.hotshot.management;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.jsonservices.AsyncTaskHotShot;
import com.example.ziku.hotshot.jsonservices.JsonHotShotsAsync;
import com.example.ziku.hotshot.services.UniversalRefresh;
import com.example.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

/**
 * Created by Ziku on 2016-08-31.
 */
public class HotShotFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.hots_shots_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        Log.d("SWIPE","Fragment HotShot Created");
        super.onActivityCreated(savedInstanceState);

        final ListView allList = (ListView)getActivity().findViewById(R.id.hot_shot_swipe_list);
        UniversalRefresh.UniwersalRefreshById(getActivity(),allList,0);
        final Activity thisActivity = getActivity();

        final SwipeRefreshLayout swipeRefreshElectronic = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_all);
        swipeRefreshElectronic.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshElectronic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        UniversalRefresh.RefreshCategoriesAndWebPages(getContext());
                        UniversalRefresh.RefreshAllIfPossible(thisActivity);
                        swipeRefreshElectronic.setRefreshing(false);
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });
    }

}
