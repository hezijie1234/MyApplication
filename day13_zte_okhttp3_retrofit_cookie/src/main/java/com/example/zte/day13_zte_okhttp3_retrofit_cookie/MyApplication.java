package com.example.zte.day13_zte_okhttp3_retrofit_cookie;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017-05-02.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
