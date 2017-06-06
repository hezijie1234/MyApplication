package com.example.zte.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by yangjw on 2017/2/15.
 */
public class MyOpenHelper extends WechatDaoMaster.DevOpenHelper {
    private static final String TAG = "androidxx";

    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
//        BikeDao.createTable(db,true);
//        BookDao.createTable(db,true);
        WechatDaoMaster.createAllTables(db,true);

    }
}
