package com.example.zte.day15_zte_retrofit_interceptor2;

import android.app.Application;
import android.content.Context;

/**
 *
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
