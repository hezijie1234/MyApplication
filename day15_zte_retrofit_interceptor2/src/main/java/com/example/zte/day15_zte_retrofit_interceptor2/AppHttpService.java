package com.example.zte.day15_zte_retrofit_interceptor2;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2017-05-03.
 */

public interface AppHttpService {
    @GET("v2/channels/101/items?ad=2&gender=1&generation=2&limit=20&offset=0")
    Observable<Gift> getData();
}
