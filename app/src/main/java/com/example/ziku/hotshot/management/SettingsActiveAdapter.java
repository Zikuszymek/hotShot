package com.example.ziku.hotshot.management;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveHotShots;
import com.example.ziku.hotshot.tables.ActiveORMmanager;
import com.example.ziku.hotshot.tables.ActiveWebSites;
import com.example.ziku.hotshot.webpages.ActiveWebPageFabric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ziku on 2016-08-29.
 */
public class SettingsActiveAdapter extends ArrayAdapter<ActiveWebSites>{
    private ListView hotShotListVIew;

    public SettingsActiveAdapter(Context context, List<ActiveWebSites> activeSettings, ListView hotShotListVIew) {
        super(context, 0, activeSettings);
        this.hotShotListVIew = hotShotListVIew;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ActiveWebSites activeWebSites = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_element,parent,false);
        }
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

                List<ActiveHotShots> activeWebSitesList = ActiveHotShots.ReturnAllActiveHotShotsActive();
                HotShotsActiveAdapter hotShotsAdapter = new HotShotsActiveAdapter(getContext(), activeWebSitesList);
                hotShotListVIew.setAdapter(null);
                hotShotListVIew.setAdapter(hotShotsAdapter);
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
            }
        });

        return convertView;
    }

}