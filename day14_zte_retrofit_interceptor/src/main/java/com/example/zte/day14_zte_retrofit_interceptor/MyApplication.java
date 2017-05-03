package com.example.zte.day14_zte_retrofit_interceptor;

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
