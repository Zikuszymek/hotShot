package com.hotshotapp.ziku.hotshot.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SwipeViewAdapter extends FragmentStatePagerAdapter{
    static private final int NUMBER_OFLISTS = 5;

    private Map<Integer,HotShotFragment> integerHotShotFragmentMap;

    public SwipeViewAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        integerHotShotFragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
//        refreshAllCreatedFragments();
        Log.d("SWIPE","swipe activities" + position);
        switch (position){
            case 0:
                return addNewHotShotFragmentIfNotExist(0);
            case 1:
                return addNewHotShotFragmentIfNotExist(1);
            case 2:
                return addNewHotShotFragmentIfNotExist(3);
            case 3:
                return addNewHotShotFragmentIfNotExist(2);
            case 4:
                return addNewHotShotFragmentIfNotExist(4);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OFLISTS;
    }

    public void refreshAllCreatedFragments(){
        for(Map.Entry<Integer,HotShotFragment> hotShotFragment : integerHotShotFragmentMap.entrySet()){
            hotShotFragment.getValue().refreshThisFragment(hotShotFragment.getKey());
        }
    }

    private HotShotFragment addNewHotShotFragmentIfNotExist(int fragmentID){
        HotShotFragment hotShotFragment;
        if(!integerHotShotFragmentMap.containsKey(fragmentID)){
            hotShotFragment = new HotShotFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(HotShotFragment.CATEGORY_TYPE,fragmentID);
            hotShotFragment.setArguments(bundle);
            integerHotShotFragmentMap.put(fragmentID,hotShotFragment);
        } else {
            hotShotFragment = integerHotShotFragmentMap.get(fragmentID);
        }

        return hotShotFragment;
    }

}
