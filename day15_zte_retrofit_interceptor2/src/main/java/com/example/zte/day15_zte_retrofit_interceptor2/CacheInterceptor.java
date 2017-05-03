package com.example.zte.day15_zte_retrofit_interceptor2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-05-02.
 */

public class CacheInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        //chain表示当前拦截器链条
        Request request = chain.request();
        request.newBuilder().cacheControl(new CacheControl.Builder().maxAge(3600, TimeUnit.SECONDS).build());
        Response response = chain.proceed(request);
        Response cacheResponse = response.newBuilder()
                .removeHeader("Pragma") //是Http协议中用来设置缓存时间的一个属性
                .removeHeader("Cache-Control") // 也是用来设置缓存的一个属性
                .header("Cache-Control", "max-age=3600") //添加缓存，并且设置缓存时间
                .build();
        return cacheResponse;
    }
    public static boolean isHaveNetwork(Context context){
        if(context != null){
            boolean flag = false;
            ConnectivityManager systemService = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
