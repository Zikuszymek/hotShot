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
 * Created by Ziku on 2016-08-31.
 */
public class HotShotFragment extends Fragment {

    public final static String CATEGORY_TYPE = "category_type";

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hots_shots_list, container, false);
        listView = (ListView) view.findViewById(R.id.hot_shot_swipe_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_all);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        super.onActivityCreated(savedInstanceState);

        UniversalRefresh.UniwersalRefreshById(getActivity(), listView, bundle.getInt(CATEGORY_TYPE));
        final Activity thisActivity = getActivity();

//        final SwipeRefreshLayout swipeRefreshElectronic = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_all);
        swipeRefreshLayout.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        UniversalRefresh.RefreshCategoriesAndWebPages(getContext());
                        UniversalRefresh.RefreshAllIfPossible(thisActivity);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });
    }

}
