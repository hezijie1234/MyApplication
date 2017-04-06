package com.example.day3_zte_okhttp_demo.api;



import com.example.day3_zte_okhttp_demo.LogUtils;
import com.example.day3_zte_okhttp_demo.api.cookie.CookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zhy on 16/3/10.
 */
public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) new NullPointerException("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        LogUtils.i("cookie size ---"+cookies.size());
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

}
