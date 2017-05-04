package com.example.zte.day14_zte_retrofit_interceptor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.TimeUnit;


import okhttp3.Cache;
import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private TextView textView;
    private Cache cache;
    private OkHttpClient client;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File cacheDir = getCacheDir();
        File cacheFile = new File(cacheDir, "mycache");
        Log.e(TAG, "onCreate: "+cacheFile.getPath() );
        cache = new Cache(cacheFile, 10 * 1024 * 1024);
        textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        CacheInterceptor interceptor = new CacheInterceptor();
        //下面设置的拦截器实现了有网读取缓存，缓存过期后重新访问网络,无网络时也是读取缓存。
        client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .baseUrl("http://api.liwushuo.com/")
                .build();
    }

    public void getData(View view) {

        AppHttpService appHttpService = retrofit.create(AppHttpService.class);
        appHttpService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Gift>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Gift gift) {
                        textView.setText(gift.toString());
                    }
                });
    }

}
