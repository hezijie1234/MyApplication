package com.example.day3_zte_okhttp_demo.api;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface LoadingListener extends OnResponseListener {
    void onLoading(long total, long progress);
}
