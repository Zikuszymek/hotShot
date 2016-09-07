package com.example.ziku.hotshot.management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.ziku.hotshot.HotShotsDatabase;
import com.example.ziku.hotshot.MainActivity;

/**
 * Created by Ziku on 2016-08-31.
 */
public class SwipeViewAdapter extends FragmentStatePagerAdapter{
    static private final int NUMBER_OFLISTS = 2;
    private HotShotsFragment hotShotsFragment;
    private SettingsFragment settingsFragment;
    private SettingsAdapter settingsAdapter;
    private HotShotsDatabase database;

    public SwipeViewAdapter(FragmentManager fragmentManager, HotShotsDatabase database){
        super(fragmentManager);
        this.database = database;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWIPE","swipe activities");
        switch (position){
            case 0:
                hotShotsFragment = new HotShotsFragment();
                return hotShotsFragment;
            case 1:
                settingsFragment = new SettingsFragment();
                return settingsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OFLISTS;
    }

}
