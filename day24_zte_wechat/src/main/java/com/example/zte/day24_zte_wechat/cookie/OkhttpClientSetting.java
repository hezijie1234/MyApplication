package com.example.zte.day24_zte_wechat.cookie;

import android.util.Log;

import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.NetUtil;
import com.example.zte.day24_zte_wechat.view.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017-05-26.
 * 这个类使用了单利模式，只会存在一个instance，呢么这个instance的属性等，就也只会存在一份
 */

public class OkhttpClientSetting {

    private OkHttpClient mClient;
    private CookieStore cookieStore;
    private static OkhttpClientSetting clientSetting;
    public static OkhttpClientSetting getInstance(){
        if(clientSetting == null){
            synchronized (OkhttpClientSetting.class){
                if(clientSetting == null){
                    clientSetting = new OkhttpClientSetting();
                    return clientSetting;
                }
            }
        }
        return clientSetting;
    }

    public OkHttpClient getmClient() {
        return mClient;
    }

    private OkhttpClientSetting() {
        clientSetting();
    }
    private CookieStore getCookieStore() {
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(MyApplication.getContext());
        }
        return cookieStore;
    }
    public void deleteCookie() {
        if (cookieStore != null) {
            cookieStore.removeAll();
        }
    }
    public boolean hasCookie(){
        List<Cookie> cookies = new PersistentCookieStore(MyApplication.getContext()).getCookies();
        if(cookies != null && cookies.size() > 0){
            for (Cookie cookie : cookies) {
                if("uid".equals(cookie.name())){
                    return true;
                }
            }
        }
        return false;
    }

    private void clientSetting() {
        File httpCacheDir = new File(MyApplication.getContext().getCacheDir(), "response");
        int cacheSize = 10 * 1024 * 1024;// 10 MiB
        Cache cache = new Cache(httpCacheDir, cacheSize);
        CookieJarImpl cookieJar = new CookieJarImpl(getCookieStore());
        mClient = new OkHttpClient.Builder()
                .addInterceptor(headInterceptor)
                .addInterceptor(netWorkInterceptor)
                .addInterceptor(new LoggingInterceptor())
                .cache(cache)
                .cookieJar(cookieJar)
                .build();
    }

    Interceptor headInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build();
            return chain.proceed(request);
        }
    };

    Interceptor netWorkInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //通过 CacheControl 控制缓存数据
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            //如果超过这个时间就会发送网络请求。
            cacheBuilder.maxAge(1, TimeUnit.HOURS);//这个是控制缓存的最大生命时间
            //在这个时间之内网络请求的缓存会被使用，如果不设置，缓存就不会被使用
            cacheBuilder.maxStale(1, TimeUnit.HOURS);//这个是控制缓存的过时时间
            CacheControl cacheControl = cacheBuilder.build();

            //设置拦截器
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(MyApplication.getContext())) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable(MyApplication.getContext())) {
                int maxAge = 0;//read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28;//tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Prama")
                        .header("Cache-Control", "poublic, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            Log.d(ConstantsUtil.TAG, "intercept: "+ String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Log.d(ConstantsUtil.TAG, "intercept: "+String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                    response.request().url(),
                    responseBody.string(),
                    (t2 - t1) / 1e6d,
                    response.headers()) );
            return response;
        }
    }
}
