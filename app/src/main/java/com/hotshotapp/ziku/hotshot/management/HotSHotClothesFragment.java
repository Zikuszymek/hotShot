package com.hotshotapp.ziku.hotshot.management;

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

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.AsyncTaskHotShot;
import com.hotshotapp.ziku.hotshot.services.UniversalRefresh;

/**
 * Created by Ziku on 2016-11-19.
 */

public class HotSHotClothesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clothes_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d("SWIPE","Fragment Others Created");
        super.onActivityCreated(savedInstanceState);
        final ListView clothesList = (ListView)getActivity().findViewById(R.id.clothes_swipe_list);
        UniversalRefresh.UniwersalRefreshById(getActivity(),clothesList,4);
        final Activity thisActivity = getActivity();

        final SwipeRefreshLayout swipeRefreshClothes = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_clothes);
        swipeRefreshClothes.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshClothes.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        UniversalRefresh.RefreshCategoriesAndWebPages(getContext());
                        UniversalRefresh.RefreshAllIfPossible(thisActivity);
                        swipeRefreshClothes.setRefreshing(false);
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });
    }
}
