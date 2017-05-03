package com.example.zte.day14_zte_retrofit_interceptor;


import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017-05-02.
 */

public interface AppHttpService {

    /**majax.action?method=getGiftList
     * @param method
     * @return
     */
    @GET("majax.action")
    Observable<DataBean> queryData(@Query("method") String method);

    @Headers("Cache-Control:public ,max-age=60")
    @GET("v2/channels/101/items?ad=2&gender=1&generation=2&limit=20&offset=0")
    Observable<Gift> getData();
}
