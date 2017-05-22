package com.example.zte.day24_zte_wechat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017-05-18.
 */

public class SharePreferenceUtil {
    private static final String SP_NAME = "common";
    private static SharePreferenceUtil mShareUtil;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharePreferenceUtil(Context context) {
        this.context = context;
        sp = this.context.getSharedPreferences(SP_NAME,Context.MODE_APPEND);
        editor = sp.edit();
    }

    public static  SharePreferenceUtil getInstance(Context context){
        if(mShareUtil == null){
            synchronized (SharePreferenceUtil.class){
                if(mShareUtil == null){
                    mShareUtil = new SharePreferenceUtil(context);
                    return mShareUtil;
                }
            }
        }
        return mShareUtil;
    }

    public void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBoolean(String key,boolean defValue){
        return sp.getBoolean(key,defValue);
    }

    public void putString(String key,String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key,String defValue){
        return sp.getString(key, defValue);
    }

    public void putInt(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key,int defValue){
        return sp.getInt(key, defValue);
    }

    public void remove(String key){
        editor.remove(key).apply();

    }

}
