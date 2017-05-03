package com.example.zte.day13_zte_okhttp3_retrofit_cookie;

import android.content.Context;

import com.example.zte.day13_zte_okhttp3_retrofit_cookie.cookie.CookieJarImpl;
import com.example.zte.day13_zte_okhttp3_retrofit_cookie.cookie.CookieStore;
import com.example.zte.day13_zte_okhttp3_retrofit_cookie.cookie.PersistentCookieStore;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-04-28.
 */

public class HttpTool {

    private static HttpTool httpTool;
    private Retrofit retrofit;
    private CookieStore cookieStore;

    public static HttpTool getInstance(Context context){
        if(httpTool == null){
            synchronized (HttpTool.class){
                if(httpTool == null){
                    httpTool = new HttpTool(context);
                }
            }
        }
        return httpTool;
    }

    public CookieStore getCookieStore() {
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(MyApplication.getContext());
        }
        return cookieStore;
    }

    /**用与登录是判断是否有cookie，以便于直接登录。
     * @return
     */
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

    /**
     * 当退出登录的时候可以调用此方法删除cookie
     */
    public void deleteCookie() {
        if (cookieStore != null) {
            cookieStore.removeAll();
        }
    }

    public HttpTool(Context context){

        CookieJarImpl cookieJar = new CookieJarImpl(getCookieStore());
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676,TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.249:8080/idcheck/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public <T>T createService(Class<T> c){
        return retrofit.create(c);
    }
}
