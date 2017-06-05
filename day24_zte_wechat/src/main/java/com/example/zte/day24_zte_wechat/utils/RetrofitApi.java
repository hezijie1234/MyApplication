package com.example.zte.day24_zte_wechat.utils;

import com.example.zte.day24_zte_wechat.module.wechat.bean.FriSearchResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.IsPhoneNum;
import com.example.zte.day24_zte_wechat.module.wechat.bean.LoginResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.RegisterResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.SendCodeBean;
import com.example.zte.day24_zte_wechat.module.wechat.bean.UserRelationshipResponse;
import com.example.zte.day24_zte_wechat.module.wechat.bean.VerifyCodeResponse;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017-05-25.
 */

public interface RetrofitApi {
    @POST("user/check_phone_available")
    Observable<IsPhoneNum> checkPhoneNum(@Body RequestBody body);

    @POST("user/send_code")
    Observable<SendCodeBean> sendCode(@Body RequestBody body);

    @POST("user/verify_code")
    Observable<VerifyCodeResponse> verifyCode(@Body RequestBody body);

    @POST("user/register")
    Observable<RegisterResponse> registerCode(@Body RequestBody body);

    @POST("user/login")
    Observable<LoginResponse> login(@Body RequestBody body);

    @GET("user/find/{region}/{phone}")
    Observable<FriSearchResponse> searchFriend(@Path("region")String region,@Path("phone")String phone);

    //获取发生过用户关系的列表
    @GET("friendship/all")
    Observable<UserRelationshipResponse> getAllUserRelationship();
}
