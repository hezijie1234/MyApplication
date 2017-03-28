package com.example.retrofit_rxjava_progressbar;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/28.
 */

public interface HttpService {
    @GET("v2/channels/{num}/items")
    Observable<SelectBean> querySelectData(@Path("num") int num, @Query("ad") int ad,@Query("gender") int gender,@Query("generation") int generation
            , @Query("limit") int limit, @Query("offset") int offset);

}
