package com.example.ziku.hotshot.management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ziku.hotshot.R;
import com.example.ziku.hotshot.tables.ActiveInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ziku on 2016-11-30.
 */

public class InfoActiveAdapter extends ArrayAdapter<ActiveInfo>{
    public InfoActiveAdapter(Context context, List<ActiveInfo> activeInfoList) {
        super(context, 0, activeInfoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ActiveInfo activeInfo = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inf_list_element,parent,false);
        }

        TextView versionText = (TextView) convertView.findViewById(R.id.info_version);
        TextView dateText = (TextView) convertView.findViewById(R.id.info_data);
        TextView nameText = (TextView) convertView.findViewById(R.id.info_name);
        TextView contentText = (TextView) convertView.findViewById(R.id.info_content);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        versionText.setText(activeInfo.version);
        dateText.setText(simpleDateFormat.format(activeInfo.infoDate));
        nameText.setText(activeInfo.infoName);
        contentText.setText(activeInfo.infoContent);

        return convertView;
    }
}
