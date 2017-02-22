package com.hotshotapp.ziku.hotshot.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SwipeViewAdapter extends FragmentStatePagerAdapter{
    static private final int NUMBER_OFLISTS = 5;

    private HotShotFragment hotShotFragment;
    private Bundle bundle;

    public SwipeViewAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWIPE","swipe activities" + position);
        bundle = new Bundle();
        hotShotFragment = new HotShotFragment();
        switch (position){
            case 0:
                bundle.putInt(HotShotFragment.CATEGORY_TYPE,0);
                hotShotFragment.setArguments(bundle);
                return hotShotFragment;
            case 1:
                bundle.putInt(HotShotFragment.CATEGORY_TYPE,1);
                hotShotFragment.setArguments(bundle);
                return hotShotFragment;
            case 2:
                bundle.putInt(HotShotFragment.CATEGORY_TYPE,2);
                hotShotFragment.setArguments(bundle);
                return hotShotFragment;
            case 3:
                bundle.putInt(HotShotFragment.CATEGORY_TYPE,3);
                hotShotFragment.setArguments(bundle);
                return hotShotFragment;
            case 4:
                bundle.putInt(HotShotFragment.CATEGORY_TYPE,4);
                hotShotFragment.setArguments(bundle);
                return hotShotFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OFLISTS;
    }

}
