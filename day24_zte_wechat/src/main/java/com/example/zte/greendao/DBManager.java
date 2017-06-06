package com.example.zte.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017-06-06.
 */

public class DBManager {

    private static DBManager manager;
    private MyOpenHelper openHelper;

    private DBManager(Context context ,String name){
        openHelper = new MyOpenHelper(context,name);
    }
    public static DBManager getInstance(Context context ,String name){
        if(manager == null){
            synchronized (DBManager.class){
                if(manager == null){
                    manager = new DBManager(context,name);
                }
            }
        }
        return manager;
    }
    public WechatDaoSession getReadableDBSession(){
        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        WechatDaoMaster androidDaoMaster = new WechatDaoMaster(readableDatabase);
        return androidDaoMaster.newSession();
    }

    public WechatDaoSession getWriteableDBSession(){
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        WechatDaoMaster androidDaoMaster = new WechatDaoMaster(writableDatabase);
        return androidDaoMaster.newSession();
    }
}
