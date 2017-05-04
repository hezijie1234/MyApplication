package com.example.zte.day16_zte_retrofit_uploading;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Administrator on 2017-05-04.
 */

public interface AppHttpService {
    @Multipart
    @POST("AppYuFaKu/uploadHeadImg")
    Observable<BaseResultEntity<UploadResulte>> uploadImage(@Part("uid")RequestBody uid, @Part("auth_key") RequestBody auth_key, @Part MultipartBody.Part file);

}
