package com.hotshotapp.ziku.hotshot.management;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private HotShotRecyclerAdapter.ActivityReactionOnAdapter activityReactionOnAdapter;

    @BindView(R.id.hot_shot_swipe_list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_all)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view_element)
    LinearLayout emptyElement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int refreshedId = (bundle.getInt(CATEGORY_TYPE));
        View view = inflater.inflate(R.layout.hots_shots_list, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotShotRecyclerAdapter = new HotShotRecyclerAdapter(getActivity(),refreshedId,emptyElement);
        recyclerView.setAdapter(hotShotRecyclerAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setColorSchemeColors(UniversalRefresh.orangeColor,UniversalRefresh.lighterOrangeColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskHotShot hotShotsAsync = new AsyncTaskHotShot(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        refreshThisFragment();
                    }
                },getContext(),true);
                hotShotsAsync.execute();
            }
        });

    }

    public void refreshThisFragment(){
        if(hotShotRecyclerAdapter!=null){
            hotShotRecyclerAdapter.refrehstDataSet();
        }
    }

}
