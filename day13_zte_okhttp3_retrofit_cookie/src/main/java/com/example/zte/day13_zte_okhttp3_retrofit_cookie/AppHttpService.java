package com.example.zte.day13_zte_okhttp3_retrofit_cookie;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017-04-28.
 */

public interface AppHttpService {
    @FormUrlEncoded
    @POST("api/account/login")
    Call<ResponseBody> queryData(@Field("account") String account,@Field("password") String password,@Field("deviceId") String deviceId);

    @FormUrlEncoded//所有的post参数都已经编码为url
    @POST("majax.action?method=getGiftList")
    Call<ResponseBody> queryList(@Field("pageno") int num);
}
