package com.example.zte.day15_zte_retrofit_interceptor2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "111";
    private Cache cache;
    private OkHttpClient client;
    private TextView textView;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File cacheDir = getCacheDir();
        textView = (TextView) findViewById(R.id.text);
        cache = new Cache(cacheDir,10*1024*1024);
        client = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CacheInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.liwushuo.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void getDataClick(View view) {
        AppHttpService appHttpService = retrofit.create(AppHttpService.class);
        appHttpService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Gift>() {
                    @Override
                    public void call(Gift dataBean) {
                        textView.setText(dataBean.toString());
                    }
                });

    }
}
