package com.hotshotapp.ziku.hotshot.management;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.jsonservices.AsyncTaskHotShot;
import com.hotshotapp.ziku.hotshot.services.UniversalRefresh;
import com.hotshotapp.ziku.hotshot.tables.ActiveHotShots;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ziku on 2016-08-31.
 */
public class HotShotFragment extends Fragment {

    public final static String CATEGORY_TYPE = "category_type";

    private HotShotRecyclerAdapter hotShotRecyclerAdapter;
    private List<ActiveHotShots> activeHotShotsList;

    @BindView(R.id.hot_shot_swipe_list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_all)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.hots_shots_list, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshThisFragment(bundle.getInt(CATEGORY_TYPE));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final int categoryType = bundle.getInt(CATEGORY_TYPE);
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        refreshThisFragment(categoryType);
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });
    }

    public void refreshThisFragment(int categoryType){
        activeHotShotsList = ActiveHotShots.ReturnAllActiveHotShotsActive(categoryType);
        hotShotRecyclerAdapter = new HotShotRecyclerAdapter(activeHotShotsList,getContext());
        recyclerView.setAdapter(hotShotRecyclerAdapter);
    }

}
