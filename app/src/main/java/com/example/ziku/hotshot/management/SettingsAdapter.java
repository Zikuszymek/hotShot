package com.example.ziku.hotshot.management;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ziku.hotshot.HotShotsDatabase;
import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.WebSiteTable;

import java.util.List;

/**
 * Created by Ziku on 2016-08-29.
 */
public class SettingsAdapter extends CursorAdapter{
    private LayoutInflater layoutInflater;
    private HotShotsDatabase database;
    private ListView hotShotListVIew;

    public SettingsAdapter(Context context, Cursor c, int flags, ListView hotShotListVIew) {
        super(context, c, flags);
        database = HotShotsDatabase.ReturnSingleInstance(context);
        this.hotShotListVIew = hotShotListVIew;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.settings_element,viewGroup,false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        final Context thisContext = context;
        final View thisView = view;
        TextView textView = (TextView) view.findViewById(R.id.settings_text_element);
        CheckBox checkBox1 = (CheckBox) view.findViewById(R.id.settings_active);
        final CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.settings_notify);

        textView.setText(cursor.getString(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NAME)));
        final int id = cursor.getInt(cursor.getColumnIndex(WebSiteTable.WebSiteColumns._ID));

        if (cursor.getInt(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.ACTIVE)) == 1)
            checkBox1.setChecked(true);
        else
            checkBox1.setChecked(false);

        if (cursor.getInt(cursor.getColumnIndex(WebSiteTable.WebSiteColumns.NOTIFICATION)) == 1)
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
                    database.SetIfWebPageIsActive(id, true);
                    checkBox2.setClickable(true);
                    checkBox2.setEnabled(true);
                } else {
                    database.SetIfWebPageIsActive(id, false);
                    checkBox2.setChecked(false);
                    checkBox2.setClickable(false);
                    checkBox2.setEnabled(false);
                }

                Cursor thisCursor = database.GetAllActiveHotShots();
                HotShotsAdapter hotShotsAdapter = new HotShotsAdapter(thisContext, thisCursor, 0);
                hotShotListVIew.setAdapter(hotShotsAdapter);
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    database.SetIfWebPAgeNotify(id, true);
                } else {
                    database.SetIfWebPAgeNotify(id, false);
                }
            }
        });
    }
}
