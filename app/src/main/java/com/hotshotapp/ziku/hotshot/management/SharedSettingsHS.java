package com.hotshotapp.ziku.hotshot.management;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ziku on 2017-01-29.
 */

public class SharedSettingsHS {

    private static final String PREFERENCES = "Settings";
    private static final String VIBRATION = "Vibration";
    private static final String SYNHRONIZED_BACKGROUND = "Background";
    private static final String SYNHRONIZED_WIFI_ONLY = "WifiOnly";
    private static final String NOTYFICATION = "Notyfication";

    public static boolean GetPreferenceBoolen(String preference, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(preference,false);
    }

    public static void SetPreferenceBoolen(boolean boob, String preference, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preference,boob);
        editor.apply();
    }
}
