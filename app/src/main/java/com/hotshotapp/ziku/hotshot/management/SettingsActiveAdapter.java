package com.hotshotapp.ziku.hotshot.management;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.hotshotapp.ziku.hotshot.R;
import com.hotshotapp.ziku.hotshot.tables.ActiveWebSites;

import java.util.List;

/**
 * Created by Ziku on 2016-08-29.
 */
public class SettingsActiveAdapter extends ArrayAdapter<ActiveWebSites>{

    private boolean isNotificationEnabled;

    public SettingsActiveAdapter(Context context, List<ActiveWebSites> activeSettings) {
        super(context, 0, activeSettings);
        isNotificationEnabled = SharedSettingsHS.GetPreferenceBoolen(context.getString(R.string.key_notyfication),context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ActiveWebSites activeWebSites = getItem(position);
        convertView = null;
        if(convertView == null) {


            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_element, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.settings_text_element);
            CheckBox checkBox1 = (CheckBox) convertView.findViewById(R.id.settings_active);
            final CheckBox checkBox2 = (CheckBox) convertView.findViewById(R.id.settings_notify);

            textView.setText(activeWebSites.webSiteName);

            if (activeWebSites.isActive)
                checkBox1.setChecked(true);
            else
                checkBox1.setChecked(false);

            if (activeWebSites.notifyUser)
                checkBox2.setChecked(true);
            else {
                checkBox2.setChecked(false);
                if (!checkBox1.isChecked()) {
                    checkBox2.setClickable(false);
                    checkBox2.setEnabled(false);
                }
            }

            if(!isNotificationEnabled){
                checkBox2.setChecked(false);
                checkBox2.setClickable(false);
                checkBox2.setEnabled(false);
            }
            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        activeWebSites.isActive = true;
                        activeWebSites.save();
                        checkBox2.setClickable(true);
                        checkBox2.setEnabled(true);
                    } else {

                        activeWebSites.isActive = false;
                        activeWebSites.notifyUser = false;
                        activeWebSites.save();
                        checkBox2.setChecked(false);
                        checkBox2.setClickable(false);
                        checkBox2.setEnabled(false);
                    }
                    notifyDataSetChanged();
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        activeWebSites.notifyUser = true;
                        activeWebSites.save();
                    } else {
                        activeWebSites.notifyUser = false;
                        activeWebSites.save();
                    }
                    notifyDataSetChanged();
                }
            });

        }
        return convertView;
    }

}
