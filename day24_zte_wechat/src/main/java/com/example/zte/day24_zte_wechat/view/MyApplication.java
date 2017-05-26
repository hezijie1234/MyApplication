package com.example.zte.day24_zte_wechat.view;

import android.app.Application;
import android.content.Context;

import com.example.zte.day24_zte_wechat.cookie.OkhttpClientSetting;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-05-25.
 */

public class MyApplication extends Application {
    private static Context context;
    public static Retrofit retrofit;

    public static Context getContext() {
        return context;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkhttpClientSetting.getInstance().getmClient())
                .build();
    }
}
