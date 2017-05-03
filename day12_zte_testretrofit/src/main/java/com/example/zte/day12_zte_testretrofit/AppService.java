package com.example.zte.day12_zte_testretrofit;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017-04-27.
 */

public interface AppService {

    @GET("v2/channels/101/items?ad=2&gender=1&generation=2&limit=20&offset=0")
    Call<ResponseBody> queryData();

    //http://www.1688wan.com
    @FormUrlEncoded//所有的post参数都已经编码为url
    @POST("majax.action?method=getGiftList")
    Call<ResponseBody> queryList(@Field("pageno") int num);

    @GET("majax.action?method=getGiftList")
    rx.Observable<DataBean> queryGiftData();
}
