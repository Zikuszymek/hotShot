package com.example.ziku.hotshot.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.ziku.hotshot.tables.ActiveCategories;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SwipeViewAdapter extends FragmentStatePagerAdapter{
    static private final int NUMBER_OFLISTS = 4;
    private HotShotFragment hotShotFragment;
    private HotShotElectronicsFragment hotShotElectronicsFragment;
    private HotShotOthersFragment hotShotOthersFragment;
    private HotShotsBooksFragment hotShotsBooksFragment;

    public SwipeViewAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWIPE","swipe activities" + position);
        switch (position){
            case 0:
                hotShotFragment = new HotShotFragment();
                return hotShotFragment;
            case 1:
                hotShotElectronicsFragment = new HotShotElectronicsFragment();
                return hotShotElectronicsFragment;
            case 2:
                hotShotOthersFragment = new HotShotOthersFragment();
                return hotShotOthersFragment;
            case 3:
                hotShotsBooksFragment = new HotShotsBooksFragment();
                return hotShotsBooksFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OFLISTS;
    }

}
