package com.hotshotapp.ziku.hotshot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.management.SharedSettingsHS;
import com.hotshotapp.ziku.hotshot.services.HotShotAlarmReceiver;

/**
 * Created by Ziku on 2017-01-31.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    SwitchPreference switchPreferenceSynchronized;
    SwitchPreference switchPreferenceWiFi;
    SwitchPreference switchPreferenceNotyfication;
    SwitchPreference switchPreferenceVibrate;
    SwitchPreference switchPreferenceUpdates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        switchPreferenceSynchronized = (SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.key_sync_background));
        switchPreferenceWiFi = (SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.key_sync_wifi));
        switchPreferenceNotyfication = (SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.key_notyfication));
        switchPreferenceVibrate = (SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.key_vibration));
        switchPreferenceUpdates = (SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.key_check_for_updates));
        swtSwitcherTextAndWifi(switchPreferenceSynchronized, switchPreferenceWiFi);
        swtSwitcherTextAndWifi(switchPreferenceNotyfication, switchPreferenceVibrate);
        setSwitcherText(switchPreferenceVibrate);
        setSwitcherText(switchPreferenceUpdates);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void setSwitcherText(SwitchPreference preference) {
        if (preference.isChecked()) {
            preference.setSummary(getString(R.string.wlaczone));
        } else {
            preference.setSummary(getString(R.string.wylaczone));
        }
    }

    private void swtSwitcherTextAndWifi(SwitchPreference preference, SwitchPreference preferenceToDisable) {
        if (preference.isChecked()) {
            preference.setSummary(getString(R.string.wlaczone));
            preferenceToDisable.setEnabled(true);
        } else {
            preference.setSummary(getString(R.string.wylaczone));
            preferenceToDisable.setEnabled(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.key_sync_background))) {
            swtSwitcherTextAndWifi(switchPreferenceSynchronized, switchPreferenceWiFi);
            if(switchPreferenceSynchronized.isChecked()){
                HotShotAlarmReceiver.SetAlarmManager(getActivity());
            } else {
                HotShotAlarmReceiver.CancelAlarmManager(getActivity());
            }
        } else if (s.equals(getString(R.string.key_notyfication))) {
            swtSwitcherTextAndWifi(switchPreferenceNotyfication, switchPreferenceVibrate);
        } else if (s.equals(getString(R.string.key_vibration))) {
            setSwitcherText(switchPreferenceVibrate);
        } else if (s.equals(getString(R.string.key_check_for_updates))){
            setSwitcherText(switchPreferenceUpdates);
        }

    }
}
