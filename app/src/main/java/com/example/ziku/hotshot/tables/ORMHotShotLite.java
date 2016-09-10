package com.example.ziku.hotshot.tables;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Ziku on 2016-09-10.
 */
public class ORMHotShotLite extends OrmLiteConfigUtil{

    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile("ormlite_config.txt");
    }
}
