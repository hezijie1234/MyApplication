package com.example.zte.day24_zte_wechat.utils;

import com.example.zte.day24_zte_wechat.module.wechat.bean.IsPhoneNum;
import com.example.zte.day24_zte_wechat.module.wechat.bean.SendCodeBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017-05-25.
 */

public interface RetrofitApi {
    @POST("user/check_phone_available")
    Observable<IsPhoneNum> checkPhoneNum(@Body RequestBody body);

    @POST("user/send_code")
    Observable<SendCodeBean> sendCode(@Body RequestBody body);
}
