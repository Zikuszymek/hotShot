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

        Log.d("SWIPE","Fragment Electronics Created");
        super.onActivityCreated(savedInstanceState);

        final ListView electronicList = (ListView)getActivity().findViewById(R.id.electronics_swipe_list);
        UniversalRefresh.UniwersalRefreshById(getActivity(),electronicList,1);
        final Activity thisActivity = getActivity();

        final SwipeRefreshLayout swipeRefreshElectronic = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_electronic);
        swipeRefreshElectronic.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshElectronic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        UniversalRefresh.RefreshAllIfPossible(thisActivity);
                        swipeRefreshElectronic.setRefreshing(false);
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });
    }
}
